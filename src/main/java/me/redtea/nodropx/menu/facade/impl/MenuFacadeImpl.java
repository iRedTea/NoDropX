package me.redtea.nodropx.menu.facade.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.menu.CachedMenu;
import me.redtea.nodropx.menu.Menu;
import me.redtea.nodropx.menu.facade.MenuFacade;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
public class MenuFacadeImpl implements MenuFacade {
    private final CachedMenu personalStorageCachedMenu;
    private final Menu confirmDropMenu;

    @Override
    public CachedMenu noDropStorage() {
        return personalStorageCachedMenu;
    }

    @Override
    public void openPersonalStorage(Player player) {
        personalStorageCachedMenu.open(player);
    }

    @Override
    public void openConfirmDrop(Player player) {
        confirmDropMenu.open(player);
    }

    @Override
    public void clearCache(Player player) {
        personalStorageCachedMenu.clearCache(player);
    }

    @Override
    public void clearAllCache() {
        personalStorageCachedMenu.clearAllCache();
    }
}
