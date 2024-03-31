package me.redtea.nodropx.service.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * Service for working with NBT tags
 */
public interface NBTService {
    /**
     * Sets the NBT tag "isNoDrop", which indicates that the item is no drop
     * @param item editable item
     * @param tag nbt tag
     * @param value tag value
     * @since 1.0.4
     * @author redtea
     */
    void setBoolTag(ItemStack item, String tag, boolean value);

    /**
     * Gets the value of the "isNoDrop" tag, which indicates that the item is no drop
     * @param item checking item
     * @param tag nbt tag
     * @return tag value
     * @since 1.0.4
     * @author redtea
     */
    boolean getBoolTag(ItemStack item, String tag);
}
