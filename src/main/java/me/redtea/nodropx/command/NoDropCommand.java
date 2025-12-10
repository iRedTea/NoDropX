package me.redtea.nodropx.command;

import dev.triumphteam.cmd.bukkit.annotation.Permission;
import dev.triumphteam.cmd.core.annotations.Command;
import dev.triumphteam.cmd.core.annotations.NamedArguments;
import dev.triumphteam.cmd.core.annotations.Optional;
import dev.triumphteam.cmd.core.annotations.Suggestion;
import dev.triumphteam.cmd.core.suggestion.SuggestionContext;
import lombok.RequiredArgsConstructor;
import lombok.val;
import me.redtea.nodropx.NoDropX;
import me.redtea.nodropx.api.facade.NoDropAPI;
import me.redtea.nodropx.menu.facade.MenuFacade;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.service.allnodrop.AllNoDropService;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import me.redtea.nodropx.service.event.EventService;
import me.redtea.nodropx.service.material.ItemStackService;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Date;
import java.util.List;

/**
 * Main command of plugin
 * <p>/nodrop - usage</p>
 * <p>/nodrop give [Player] [Material] [Amount (optional)]</p>
 */
@Command("nodrop")
@RequiredArgsConstructor
public class NoDropCommand {
    private final Messages messages;
    private final NoDropAPI noDropAPI;
    private final MenuFacade menuFacade;
    private final NoDropX plugin;
    private final ItemStackService itemStackService;
    private final DropConfirmService confirmService;
    private final EventService eventService;
    private final AllNoDropService allNoDropService;

    @Command
    public void onDefault(final CommandSender sender) {
        messages.get("usage").send(sender);
    }



    @Command("give")
    @Permission("nodropx.give")
    @NamedArguments("query-parameters")
    //@TabComplete(complete = {"#players", "#materials", "#range:1-64"})
    public void onGive(final CommandSender sender,
                       final Player player,
                       @Suggestion("allmaterials") final String material,
                       @Optional final Integer amount) {
        try {
            ItemStack item = noDropAPI.getNoDrop(material, amount == null || amount == 0 ? 1 : amount);
            player.getInventory().addItem(item);


            messages.get("give.success")
                    .replaceAll("%item%", material)
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", amount == null ? "1" : amount.toString())
                    .send(sender);
        } catch (Throwable e) {
            e.printStackTrace();
            messages.get("give.fail").send(sender);
        }

    }

    @Command("reload")
    @Permission("nodropx.reload")
    public void onReload(final CommandSender sender) {
        long time = new Date().getTime();
        plugin.onReload();
        messages.get("reload").replaceAll("%time%", String.valueOf(new Date().getTime() - time)).send(sender);
    }

    @Command("givestorage")
    @Permission("nodropx.giveStorage")
    @NamedArguments("query-parameters")
    //@TabComplete(complete = {"#players", "#materials", "#range:1-64"})
    public void onGiveStorage(final CommandSender sender,
                              final String playerName,
                              final String material,
                              @Optional final Integer amount) {
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
                    .replaceAll("%amount%", amount == null ? "1" : amount.toString())
                    .send(sender);
        } catch (Throwable e) {
            messages.get("giveToStorage.fail").send(sender);
        }

    }

    @Command("ignoreconfirm")
    @Permission("nodropx.ignoreConfirm")
    public void onIgnoreConfirm(final CommandSender sender, @Optional Player target) {
        if(target == null) {
            if(!(sender instanceof Player)) {
                messages.get("error").send(sender);
            } else {
                messages.get("ignoreConfirm.on").send(sender);
                confirmService.forceConfirm((Player) sender);
            }
        } else {
            confirmService.forceConfirm(target);
        }
    }

    @Command("ignoreconfirmtimed")
    @Permission("nodropx.ignoreConfirm.timed")
    public void onIgnoreConfirmTimed(final CommandSender sender, Integer sec, @Optional Player target) {
        if(target == null) {
            if(!(sender instanceof Player)) {
                messages.get("error").send(sender);
            } else {
                confirmService.forceConfirm((Player) sender);
                try {
                    Bukkit.getScheduler().runTaskLater(plugin,() -> confirmService.removeForceConfirm((Player) sender),20L*sec);
                    messages.get("ignoreConfirm.on").send(sender);
                } catch (UnsupportedOperationException e) {
                    sender.sendMessage(ChatColor.RED + "This operation didn't supports on your environment");
                }
            }
        } else {
            confirmService.forceConfirm(target);
            try {
                Bukkit.getScheduler().runTaskLater(plugin,() -> confirmService.removeForceConfirm(target),20L*sec);
            } catch (UnsupportedOperationException e) {
                sender.sendMessage(ChatColor.RED + "This operation didn't supports on your environment");
            }
        }
    }

    @Command("noignoreconfirm")
    @Permission("nodropx.noIgnoreConfirm")
    public void onNoIgnoreConfirm(final CommandSender sender, @Optional Player target) {
        if(target == null) {
            if(!(sender instanceof Player)) {
                messages.get("error").send(sender);
            } else {
                confirmService.removeForceConfirm((Player) sender);
                messages.get("ignoreConfirm.off").send(sender);
            }
        } else {
            confirmService.removeForceConfirm(target);
        }
    }
    @Command("applyhand")
    @Permission("nodropx.applyhand")
    public void onApplyHand(final Player sender) {
        try {
            val event = eventService.callPlayerChangeNoDropEvent(sender, sender.getInventory().getItemInMainHand(), true);
            noDropAPI.setNoDrop(sender.getInventory().getItemInMainHand(), event.getNewValue());
            messages.get("applyHand.success").send(sender);
        } catch (Throwable e) {
            messages.get("applyHand.fail").send(sender);
        }
    }

    @Command(value = "gui", alias = "storage")
    @Permission("nodropx.gui")
    public void onGui(final Player sender) {
        menuFacade.openPersonalStorage(sender);
    }

    @Nullable
    private OfflinePlayer getOfflinePlayer(final String name) {
        for(OfflinePlayer player : Bukkit.getOfflinePlayers()) {
            if(player.getName() != null && player.getName().equalsIgnoreCase(name)) return player;
        }
        return null;
    }

    @Command("giveallnodrop")
    @Permission("nodropx.giveallnodrop")
    @NamedArguments("query-parameters")
  //  @TabComplete(complete = {"#players", "#materials", "#range:1-64"})
    public void onGiveAllNoDrop(final CommandSender sender,
                       final Player player,
                       final String material,
                       @Optional final Integer amount) {
        try {
            noDropAPI.giveAllNoDrop(player, material, amount == null || amount == 0 ? 1 : amount);
            messages.get("give.success")
                    .replaceAll("%item%", material)
                    .replaceAll("%player%", player.getName())
                    .replaceAll("%amount%", amount == null ? "1" : amount.toString())
                    .send(sender);
        } catch (Throwable e) {
            messages.get("give.fail").send(sender);
        }
    }
}
