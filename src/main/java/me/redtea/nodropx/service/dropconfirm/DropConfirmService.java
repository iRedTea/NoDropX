package me.redtea.nodropx.service.dropconfirm;

import org.bukkit.entity.Player;

public interface DropConfirmService {
    boolean isConfirmed(Player player);
    void confirm(Player player);
    void resetConfirm(Player player);

    void forceConfirm(Player player);
    void removeForceConfirm(Player player);
}
