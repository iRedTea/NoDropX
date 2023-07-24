package me.redtea.nodropx.factory.item;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface NoDropItemFactory {
    ItemStack createNoDrop(@NotNull Material material, int amount);
}
