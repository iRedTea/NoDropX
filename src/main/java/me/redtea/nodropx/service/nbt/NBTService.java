package me.redtea.nodropx.service.nbt;

import org.bukkit.inventory.ItemStack;

/**
 * Service for working with NBT tags
 */
public interface NBTService {
    /**
     * Sets the NBT tag "isNoDrop", which indicates that the item is no drop
     * @param item editable item
     * @param isNoDrop tag value
     * @since 1.0.0
     * @author redtea
     */
    void setNoDropTag(ItemStack item, boolean isNoDrop);

    /**
     * Gets the value of the "isNoDrop" tag, which indicates that the item is no drop
     * @param item checking item
     * @return tag value
     * @since 1.0.0
     * @author redtea
     */
    boolean getNoDropTag(ItemStack item);
}
