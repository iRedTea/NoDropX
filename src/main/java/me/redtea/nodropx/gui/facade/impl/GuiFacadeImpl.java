package me.redtea.nodropx.gui.facade.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.gui.facade.GuiFacade;
import me.redtea.nodropx.gui.pages.StorageGui;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class GuiFacadeImpl implements GuiFacade {
    private final StorageGui storageGui;

    @Override
    public void openNoDropStorage(Player player) {
        storageGui.open(player);
    }

    @Override
    public void clearCache(Player player) {
        storageGui.clearCache(player);
    }

    @Override
    public void clearAllCache() {
        storageGui.clearAllCache();
    }
}
