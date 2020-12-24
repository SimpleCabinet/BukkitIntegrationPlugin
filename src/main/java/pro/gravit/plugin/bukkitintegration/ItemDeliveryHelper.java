package pro.gravit.plugin.bukkitintegration;

import ca.momothereal.mojangson.ex.MojangsonParseException;
import ca.momothereal.mojangson.value.MojangsonArray;
import ca.momothereal.mojangson.value.MojangsonCompound;
import ca.momothereal.mojangson.value.MojangsonValue;
import me.dpohvar.powernbt.nbt.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pro.gravit.plugin.bukkitintegration.lk.event.UserItemDeliveryEvent;
import pro.gravit.utils.helper.LogHelper;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.util.concurrent.atomic.AtomicInteger;

public class ItemDeliveryHelper {
    public static boolean isPowerNBTLoaded() {
        try {
            Class.forName("me.dpohvar.powernbt.nbt.NBTContainerItem");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    public static int deliveryItemToPlayer(Player player, UserItemDeliveryEvent.OrderSystemInfo orderSystemInfo, int part)
    {
        ItemStack stack = createItemStackFromInfo(orderSystemInfo, part);
        AtomicInteger rejectedPart = new AtomicInteger();
        player.getInventory().addItem(stack).forEach((k,v) -> {
            rejectedPart.addAndGet(v.getAmount());
        });
        return rejectedPart.get();
    }

    @SuppressWarnings("use-deprecated")
    public static ItemStack createItemStackFromInfo(UserItemDeliveryEvent.OrderSystemInfo info, int part)
    {
        ItemStack itemStack;
        Material material = getMaterial(info.itemId);
        try {
            itemStack = new ItemStack(material, part, info.itemExtra != null ? Short.parseShort(info.itemExtra) : 0);
        } catch (Throwable e) {
            if(LogHelper.isDevEnabled()) {
                LogHelper.error(e);
            }
            itemStack = new ItemStack(material);
            itemStack.setAmount(part);
        }
        itemStack.setAmount(part);
        if(info.itemNbt != null && isPowerNBTLoaded()) {
            itemStack = addNBTTag(itemStack, info.itemNbt);
        }
        if(info.enchants != null) {
            for(UserItemDeliveryEvent.OrderSystemInfo.OrderSystemEnchantInfo e : info.enchants) {
                Enchantment ench = getEnchantment(e.name);
                if(ench == null) continue;
                itemStack.addUnsafeEnchantment(ench, e.level);
            }
        }
        return itemStack;
    }

    public static Material getMaterial(String name) {
        String[] space = name.split(":");
        String namespace;
        String key;
        if(space.length < 2) {
            namespace = "minecraft";
            key = name;
        } else {
            namespace = space[0];
            key = space[1];
        }
        key = key.toUpperCase();
        try {
            int id = Integer.parseInt(key);
            Material material = null;
            try {
                 material = (Material) MethodHandles.lookup().findStatic(Material.class, "getMaterial", MethodType.methodType(Material.class, int.class)).invoke(id);
                 if(material != null) {
                     LogHelper.dev("For name %s: use private getMaterial(int) method", name);
                 }
            } catch (Throwable i) {
                if(LogHelper.isDevEnabled()) {
                    LogHelper.error(i);
                }
            }
            if(material == null) {
                Constructor<Material> constructor = Material.class.getDeclaredConstructor(int.class);
                constructor.setAccessible(true);
                material = constructor.newInstance(id);
                LogHelper.dev("For name %s: use private constructor", name);
            }
            return material;
        } catch (Throwable i) {
            if (!(i instanceof NumberFormatException)) {
                if(LogHelper.isDevEnabled()) {
                    LogHelper.error(i);
                }
            }
        }
        if(namespace != null) {
            Material material = Material.getMaterial(namespace.toUpperCase()+"_"+key);
            if(material != null) return material;
        }
        Material material = Material.getMaterial(key);
        if(material == null) {
            throw new RuntimeException(String.format("Item Material %s not found", name));
        }
        return material;
    }

    public static ItemStack toCraftItemStack(ItemStack source) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 9);
        inventory.addItem(source);
        return inventory.getItem(0);
    }

    public static ItemStack addNBTTag(ItemStack source, String nbtString) {
        ItemStack stack = toCraftItemStack(source);
        NBTContainerItem nbtContainerItem = new NBTContainerItem(stack);
        MojangsonCompound compound = new MojangsonCompound();
        try {
            compound.read(nbtString);
            nbtContainerItem.setTag(mojangsonToNBT(compound));
        } catch (MojangsonParseException e) {
            LogHelper.error(e);
        }
        return stack;
    }

    public static NBTBase mojangsonToNBT(MojangsonValue<?> value) {
        if(value instanceof MojangsonCompound) {
            NBTTagCompound compound = new NBTTagCompound();
            ((MojangsonCompound) value).forEach((k,v) -> {
                compound.put(k, mojangsonToNBT(v));
            });
            return compound;
        }
        else if(value instanceof MojangsonArray<?>) {
            NBTTagList list = new NBTTagList();
            ((MojangsonArray<?>) value).forEach((v) -> {
                list.add(mojangsonToNBT(v));
            });
            return list;
        }
        else {
            Object o = value.getValue();
            if(o instanceof String) {
                return new NBTTagString((String) o);
            }
            else if(o instanceof Integer) {
                return new NBTTagInt((Integer) o);
            }
            else if(o instanceof Long) {
                return new NBTTagLong((Long) o);
            }
            else if(o instanceof Short) {
                return new NBTTagShort((Short) o);
            }
            else if(o instanceof Byte) {
                return new NBTTagByte((Byte) o);
            }
            else if(o instanceof Float) {
                return new NBTTagFloat((Float) o);
            }
            else if(o instanceof Double) {
                return new NBTTagDouble((Double) o);
            }
            else {
                throw new RuntimeException(String.format("Mojangson to NBT unknown type: %s", o.getClass().getName()));
            }
        }
    }

    @SuppressWarnings("use-deprecated")
    public static Enchantment getEnchantment(String name) {
        String[] space = name.split(":");
        String namespace;
        String key;
        if(space.length < 2) {
            namespace = "minecraft";
            key = name;
        } else {
            namespace = space[0];
            key = space[1];
        }
        key = key.toUpperCase();
        Enchantment ench;
        try {
            int id = Integer.parseInt(key);
            ench = (Enchantment) MethodHandles.lookup().findStatic(Enchantment.class, "getById", MethodType.methodType(Enchantment.class, int.class)).invoke(id);
        } catch (Throwable i) {
            if (!(i instanceof NumberFormatException)) {
                if(LogHelper.isDevEnabled()) {
                    LogHelper.error(i);
                }
            }
        }
        try {
            Class.forName("org.bukkit.NamespacedKey");
            try {
                ench = Enchantment.getByKey(new NamespacedKey(namespace, key));
            } catch (Throwable ex) {
                try {
                    ench = (Enchantment) MethodHandles.lookup().findStaticGetter(Enchantment.class, key, Enchantment.class).invoke();
                } catch (Throwable ignored) {
                    ench = null;
                }
            }
        } catch (ClassNotFoundException e) {
            ench = Enchantment.getByName(key);
        }
        if(ench == null) {
            LogHelper.warning("Ench %s:%s not found", namespace, key);
        }
        return ench;
    }
}
