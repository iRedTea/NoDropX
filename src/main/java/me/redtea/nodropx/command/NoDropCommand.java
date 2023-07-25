package me.redtea.nodropx.command;

import lombok.RequiredArgsConstructor;
import lombok.val;
import me.mattstudios.mf.annotations.*;
import me.mattstudios.mf.base.CommandBase;
import me.redtea.nodropx.NoDropX;
import me.redtea.nodropx.api.facade.NoDropAPI;
import me.redtea.nodropx.gui.facade.GuiFacade;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.service.material.ItemStackService;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Date;

/**
 * Main command of plugin
 * <p>/nodrop - usage</p>
 * <p>/nodrop give [Player] [Material] [Amount (optional)]</p>
 */
@Command("nodrop")
@RequiredArgsConstructor
public class NoDropCommand extends CommandBase {
    private final Messages messages;
    private final NoDropAPI noDropAPI;
    private final GuiFacade guiFacade;
    private final NoDropX plugin;
    private final ItemStackService itemStackService;

    @Default
    public void onDefault(final CommandSender sender) {
        messages.get("usage").send(sender);
    }

    @SubCommand("give")
    @Permission("nodropx.give")
    @Completion({"#players", "#materials", "#range:1-64"})
    public void onGive(final CommandSender sender,
                       final Player player,
                       final String material,
                       @Optional final Integer amount) {
        try {
            noDropAPI.giveNoDrop(player, material, amount == null || amount == 0 ? 1 : amount);
            messages.get("give.success")
                    .replaceAll("%item%", material)
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", amount == null ? "1" : amount.toString())
                    .send(sender);
        } catch (Throwable e) {
            messages.get("give.fail").send(sender);
        }

    }

    @SubCommand("reload")
    @Permission("nodropx.reload")
    public void onReload(final CommandSender sender) {
        long time = new Date().getTime();
        plugin.onReload();
        messages.get("reload").replaceAll("%time%", String.valueOf(new Date().getTime() - time)).send(sender);
    }

    @SubCommand("giveStorage")
    @Permission("nodropx.giveStorage")
    @Completion({"#players", "#materials", "#range:1-64"})
    public void onGiveStorage(final CommandSender sender,
                              String playerName,
                              final String material,
                              @Optional Integer amount) {
        try {
            val item = noDropAPI.setNoDrop(itemStackService.get(material, amount == null || amount == 0 ? 1 : amount), true);
            val player = getOfflinePlayer(playerName);
            if(player == null) {
                messages.get("noExists").send(sender);
                return;
            }
            noDropAPI.getStorageManipulator(player).add(item);
            messages.get("giveToStorage.success")
                    .replaceAll("%item%", material)
                    .replaceAll("%player%", playerName)
                    .replaceAll("%amount%", amount == null ? "0" : amount.toString())
                    .send(sender);
        } catch (Throwable e) {
            messages.get("giveToStorage.fail").send(sender);
        }
    }

    @SubCommand("applyHand")
    @Permission("nodropx.applyhand")
    public void onApplyHand(final Player sender) {
        try {
            noDropAPI.setNoDrop(sender.getInventory().getItemInMainHand(), true);
            messages.get("applyHand.success").send(sender);
        } catch (Throwable e) {
            messages.get("applyHand.fail").send(sender);
        }
    }

    @SubCommand("gui")
    @Permission("nodropx.gui")
    public void onGui(final Player sender) {
        guiFacade.openNoDropStorage(sender);
    }

    @Nullable
    private OfflinePlayer getOfflinePlayer(String name) {
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if(player.getName() != null && player.getName().equalsIgnoreCase(name)) return player;
        }
        return null;
    }
}
