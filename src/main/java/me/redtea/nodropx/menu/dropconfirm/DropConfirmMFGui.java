package me.redtea.nodropx.menu.dropconfirm;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.menu.Menu;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class DropConfirmMFGui implements Menu {
    private final Gui gui;
    private final Set<UUID> inMenu = new HashSet<>();
    private final DropConfirmService dropConfirmService;
    private final Plugin plugin;
    public DropConfirmMFGui(Messages messages, DropConfirmService dropConfirmService, Plugin plugin) {
        gui = Gui.gui()
                .rows(1)
                .title(messages.get("gui.confirm.title").asComponent())
                .disableAllInteractions()
                .create();
        this.plugin = plugin;
        gui.setItem(1, ItemBuilder.from(messages.get("gui.confirm.yesButton.material").toMaterial())
                .name(messages.get("gui.confirm.yesButton.name").asComponent())
                .lore(messages.get("gui.confirm.yesButton.lore").asComponent())
                .asGuiItem((event -> {
                    dropConfirmService.confirm((Player) event.getWhoClicked());
                    event.getWhoClicked().dropItem(true);
                    event.getWhoClicked().closeInventory();
                })));
        gui.setItem(7, ItemBuilder.from(messages.get("gui.confirm.noButton.material").toMaterial())
                .name(messages.get("gui.confirm.noButton.name").asComponent())
                .lore(messages.get("gui.confirm.noButton.lore").asComponent())
                .asGuiItem((event -> event.getWhoClicked().closeInventory())));
        gui.setCloseGuiAction((e) -> {
            inMenu.remove(e.getPlayer().getUniqueId());
        });
        this.dropConfirmService = dropConfirmService;
    }

    @Override
    public void open(Player player) {
        if(inMenu.contains(player.getUniqueId())) {
            dropConfirmService.forceConfirm(player);
            player.closeInventory();
            try {
                Bukkit.getScheduler().runTaskLater(plugin,
                        () -> dropConfirmService.removeForceConfirm(player),
                        20*15L);
            } catch (Throwable e) {
                plugin.getLogger().severe("Can not start task. Maybe will bugs. Do you run on Folia?");
                e.printStackTrace();
            }
            return;
        }
        inMenu.add(player.getUniqueId());
        gui.open(player);
    }
}
