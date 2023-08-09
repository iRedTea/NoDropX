package me.redtea.nodropx.menu;

import org.bukkit.entity.Player;

public interface CachedMenu extends Menu {
    void clearCache(Player player);
    void clearAllCache();
}
