package me.redtea.nodropx.service.storage;

import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.UUID;

public interface StorageService {
    void addItem(UUID playerId, ItemStack item);
    void removeItem(UUID playerId, ItemStack item);
    List<ItemStack> getItems(UUID playerId);
}
