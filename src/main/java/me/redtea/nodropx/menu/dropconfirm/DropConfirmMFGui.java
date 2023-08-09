package me.redtea.nodropx.menu.dropconfirm;

import dev.triumphteam.gui.builder.item.ItemBuilder;
import dev.triumphteam.gui.guis.Gui;
import me.redtea.nodropx.libs.message.container.Messages;
import me.redtea.nodropx.menu.Menu;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class DropConfirmMFGui implements Menu {
    private final Gui gui;
    public DropConfirmMFGui(Messages messages, DropConfirmService dropConfirmService) {
        gui = Gui.gui()
                .rows(1)
                .title(messages.get("gui.confirm.title").asComponent())
                .disableAllInteractions()
                .create();
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
    }

    @Override
    public void open(Player player) {
        gui.open(player);
    }
}
