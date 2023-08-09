package me.redtea.nodropx.service.respawn.impl;

import me.redtea.nodropx.service.respawn.RespawnService;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class NullRespawnService implements RespawnService {
    @Override
    public void giveSavedItems(Player player) {

    }

    @Override
    public void saveItemsBeforeDeath(PlayerDeathEvent event) {

    }
}
