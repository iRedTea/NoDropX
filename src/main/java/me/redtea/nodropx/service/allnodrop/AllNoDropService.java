package me.redtea.nodropx.service.allnodrop;

import org.bukkit.inventory.ItemStack;

/**
 * Service for checking all no drop items
 * @since 1.0.4
 * @author redtea
 */
public interface AllNoDropService {
    /**
     * Set no drop to item
     * @param item the item who's no drop property are changing
     * @param isAllNoDrop will the item can drop from inventory on death?
     * @author redtea
     * @since 1.0.4
     */
    void setAllNoDrop(ItemStack item, boolean isAllNoDrop);
    /**
     * Check if item is NoDropAll
     * @param item the item being checked
     * @return true if item is NoDropAll
     * @author redtea
     * @since 1.0.4
     */
    boolean isAllNoDrop(ItemStack item);
}
