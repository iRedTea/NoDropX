package me.redtea.nodropx.api.facade.manipulator;

import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface StorageManipulator {
    /**
     * Adds no drop item to player storage
     * @param item - item that will add
     * @author redtea
     * @since 1.0.0
     */
    void add(@NotNull ItemStack item);
    /**
     * Remove no drop item to player storage
     * @param item - item that will remove
     * @author redtea
     * @since 1.0.0
     */
    void remove(@NotNull ItemStack item);

    /**
     * @return items in storage
     * @author redtea
     * @since 1.0.0
     */
    @NotNull List<ItemStack> all();
}
