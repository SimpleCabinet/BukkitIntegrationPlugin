package pro.gravit.plugin.bukkitintegration.nbt;

import org.bukkit.inventory.ItemStack;

public interface NBTHelper {
    ItemStack addNBTTag(ItemStack source, String nbtString);

    static boolean isPowerNBTLoaded() {
        try {
            Class.forName("me.dpohvar.powernbt.nbt.NBTContainerItem");
            return true;
        } catch (ClassNotFoundException e) {
            return false;
        }
    }
    static NBTHelper newInstance() {
        if(isPowerNBTLoaded()) {
            return new PowerNBTHelper();
        }
        return new ItemMetaNBTHelper();
    }
}
