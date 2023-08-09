package me.redtea.nodropx.listener.menu;

import me.redtea.nodropx.menu.storage.singlepage.SinglePageController;
import me.redtea.nodropx.menu.storage.singlepage.SinglePageGui;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

public class SinglePageGuiHandler implements Listener {
    private final SinglePageController controller;
    private final SinglePageGui gui;

    public SinglePageGuiHandler(SinglePageController controller, SinglePageGui gui) {
        this.controller = controller;
        this.gui = gui;

    }


    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if(gui.isStorageGui(event.getInventory())) {
            if(event.getAction() == InventoryAction.MOVE_TO_OTHER_INVENTORY) controller.onClick(event);
        }
        if(gui.isStorageGui(event.getClickedInventory()) && event.getWhoClicked() instanceof Player) {
            controller.onClick(event);
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if(gui.isStorageGui(event.getInventory()) && event.getPlayer() instanceof Player) {
            controller.onClose(event);
        }
    }

}
