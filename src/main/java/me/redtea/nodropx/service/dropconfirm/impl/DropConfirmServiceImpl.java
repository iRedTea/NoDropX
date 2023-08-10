package me.redtea.nodropx.service.dropconfirm.impl;

import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import org.bukkit.entity.Player;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DropConfirmServiceImpl implements DropConfirmService {
    private Set<UUID> confirmed = new HashSet<>();
    private Set<UUID> force = new HashSet<>();
    @Override
    public boolean isConfirmed(Player player) {
        if(force.contains(player.getUniqueId()) || player.isDead()) return true;
        return confirmed.contains(player.getUniqueId());
    }

    @Override
    public void confirm(Player player) {
        confirmed.add(player.getUniqueId());
    }

    @Override
    public void resetConfirm(Player player) {
        confirmed.remove(player.getUniqueId());
    }

    @Override
    public void forceConfirm(Player player) {
        force.add(player.getUniqueId());
    }

    @Override
    public void removeForceConfirm(Player player) {
        force.remove(player.getUniqueId());
    }

}
