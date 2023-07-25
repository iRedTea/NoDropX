package me.redtea.nodropx.api.facade.impl;

import me.redtea.nodropx.api.facade.NoDropAPI;
import me.redtea.nodropx.api.facade.manipulator.StorageManipulator;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.factory.item.NoDropItemFactory;
import me.redtea.nodropx.libs.carcadex.repo.Repo;
import me.redtea.nodropx.service.material.ItemStackService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.service.storage.StorageService;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class NoDropXFacade implements NoDropAPI {

    private final NoDropService noDropService;
    private final NoDropItemFactory itemFactory;
    private final StorageService storageService;
    private final Repo<String, List<Integer>> capacity;
    private final ItemStackService itemStackService;

    @Override
    public ItemStack setNoDrop(@NotNull ItemStack item, boolean isNoDrop) {
        noDropService.setNoDrop(item, isNoDrop);
        return item;
    }

    @Override
    public boolean isNoDrop(@NotNull ItemStack item) {
        return noDropService.isNoDrop(item);
    }

    @Override
    @NotNull
    public ItemStack getNoDrop(@NotNull Material material, int amount) {
        return itemFactory.createNoDrop(material, amount);
    }

    @Override
    public void giveNoDrop(@NotNull HumanEntity entity, @NotNull ItemStack item) {
        entity.getInventory().addItem(setNoDrop(item, true));
    }

    @Override
    public void giveNoDrop(@NotNull HumanEntity entity, @NotNull Material material, int amount) {
        entity.getInventory().addItem(getNoDrop(material, amount));
    }

    @Override
    public @NotNull StorageManipulator getStorageManipulator(@NotNull UUID playerUniqueId) {
        return new StorageManipulator() {
            @Override
            public void add(@NotNull ItemStack item) {
                storageService.addItem(playerUniqueId, item);
            }

            @Override
            public void remove(@NotNull ItemStack item) {
                storageService.removeItem(playerUniqueId, item);
            }

            @Override
            public @NotNull List<ItemStack> all() {
                return storageService.getItems(playerUniqueId);
            }
        };
    }

    @Override
    public @NotNull Collection<Integer> getCapacitySlots(Material material) {
        return capacity.get(material.name()).orElse(new ArrayList<>());
    }

    @Override
    public @NotNull Collection<Integer> getCapacitySlots(String material) {
        return getCapacitySlots(itemStackService.get(material, 1).getType());
    }

    @Override
    public void giveNoDrop(@NotNull HumanEntity entity, @NotNull String material, int amount) {
        giveNoDrop(entity, itemStackService.get(material, amount));
    }

    @Override
    public @NotNull ItemStack getNoDrop(@NotNull String material, int amount) {
        return setNoDrop(itemStackService.get(material, amount), true);
    }
}
