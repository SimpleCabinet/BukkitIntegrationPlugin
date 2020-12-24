package pro.gravit.plugin.bukkitintegration.nbt;

import ca.momothereal.mojangson.ex.MojangsonParseException;
import ca.momothereal.mojangson.value.MojangsonArray;
import ca.momothereal.mojangson.value.MojangsonCompound;
import ca.momothereal.mojangson.value.MojangsonValue;
import me.dpohvar.powernbt.nbt.*;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import pro.gravit.utils.helper.LogHelper;

public class PowerNBTHelper implements NBTHelper {
    public ItemStack toCraftItemStack(ItemStack source) {
        Inventory inventory = Bukkit.getServer().createInventory(null, 9);
        inventory.addItem(source);
        return inventory.getItem(0);
    }

    public ItemStack addNBTTag(ItemStack source, String nbtString) {
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

    public NBTBase mojangsonToNBT(MojangsonValue<?> value) {
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
}
