package me.redtea.nodropx.gui.facade;

import org.bukkit.entity.Player;

public interface GuiFacade {
    void openNoDropStorage(Player player);
    void clearCache(Player player);
    void clearAllCache();
}
