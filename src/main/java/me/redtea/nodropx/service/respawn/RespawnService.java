package me.redtea.nodropx.service.respawn;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public interface RespawnService {
    void giveSavedItems(Player player);
    void saveItemsBeforeDeath(PlayerDeathEvent event);
}
