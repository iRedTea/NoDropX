package me.redtea.nodropx.service.nodrop;

import org.bukkit.inventory.ItemStack;

/**
 * Service for changing and checking no drop status of item
 */
public interface NoDropService {
    /**
     * Set no drop to item
     * @param item the item who's no drop property are changing
     * @param isNoDrop will the item can drop from inventory on death?
     * @author redtea
     * @since 1.0.0
     */
    void setNoDrop(ItemStack item, boolean isNoDrop);

    /**
     * Check if item is no drop item
     * @param item the item being checked
     * @return true if item can not drop from inventory on death
     * @author redtea
     * @since 1.0.0
     */
    boolean isNoDrop(ItemStack item);
}
