package me.redtea.nodropx.service.dropconfirm.impl;

import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import org.bukkit.entity.Player;

public class DropConfirmNull implements DropConfirmService {
    @Override
    public boolean isConfirmed(Player player) {
        return true;
    }

    @Override
    public void confirm(Player player) {

    }

    @Override
    public void resetConfirm(Player player) {

    }

    @Override
    public void forceConfirm(Player player) {

    }

    @Override
    public void removeForceConfirm(Player player) {

    }
}
