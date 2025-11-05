package me.redtea.nodropx.menu.facade.impl;

import me.redtea.nodropx.menu.CachedMenu;
import me.redtea.nodropx.menu.facade.MenuFacade;
import org.bukkit.entity.Player;

public class NullMenuFacadeImpl implements MenuFacade {
    @Override
    public CachedMenu noDropStorage() {
        return new CachedMenu() {
            @Override
            public void clearCache(Player player) {

            }

            @Override
            public void clearAllCache() {

            }

            @Override
            public void open(Player player) {

            }
        };
    }

    @Override
    public void openPersonalStorage(Player player) {

    }

    @Override
    public void openConfirmDrop(Player player) {

    }

    @Override
    public void clearCache(Player player) {

    }

    @Override
    public void clearAllCache() {

    }
}
