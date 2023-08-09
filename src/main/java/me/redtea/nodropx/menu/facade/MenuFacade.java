package me.redtea.nodropx.menu.facade;

import me.redtea.nodropx.menu.CachedMenu;
import org.bukkit.entity.Player;

public interface MenuFacade {
    CachedMenu noDropStorage();
    void openPersonalStorage(Player player);
    void openConfirmDrop(Player player);
    void clearCache(Player player);
    void clearAllCache();
}
