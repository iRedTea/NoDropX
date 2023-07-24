package me.redtea.nodropx.factory.item.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.factory.item.NoDropItemFactory;
import me.redtea.nodropx.service.nodrop.NoDropService;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

@RequiredArgsConstructor
public class NoDropItemFactoryImpl implements NoDropItemFactory {
    private final NoDropService noDropService;

    @Override
    @NotNull
    public ItemStack createNoDrop(@NotNull Material material, int amount) {
        ItemStack result = new ItemStack(material, amount);
        noDropService.setNoDrop(result, true);
        return result;
    }
}
