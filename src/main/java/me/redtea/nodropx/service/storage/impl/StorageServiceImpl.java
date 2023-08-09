package me.redtea.nodropx.service.storage.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.menu.facade.MenuFacade;
import me.redtea.nodropx.libs.carcadex.repo.MutableRepo;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.service.storage.StorageService;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final MenuFacade menuFacade;
    private final MutableRepo<UUID, List<ItemStack>> pagesRepo;
    private final NoDropService noDropService;

    @Override
    public void addItem(UUID playerId, ItemStack item) {
        List<ItemStack> items = new ArrayList<>(pagesRepo.get(playerId).orElse(new ArrayList<>()));
        if(!noDropService.isNoDrop(item)) throw new IllegalArgumentException("Item must be no drop!");
        items.add(item);
        pagesRepo.update(playerId, items);
        menuFacade.clearCache(Bukkit.getPlayer(playerId));
    }

    @Override
    public void removeItem(UUID playerId, ItemStack item) {
        List<ItemStack> items = pagesRepo.get(playerId).orElse(null);
        if(items == null) return;
        items.remove(item);
        pagesRepo.update(playerId, items);
        menuFacade.clearCache(Bukkit.getPlayer(playerId));
    }

    @Override
    public List<ItemStack> getItems(UUID playerId) {
        return pagesRepo.get(playerId).orElse(new ArrayList<>());
    }
}
