package me.redtea.nodropx.listener;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.service.event.EventService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@RequiredArgsConstructor
public class InventoryHandler implements Listener {
    private final EventService eventService;

    @EventHandler
    public void onMove(InventoryClickEvent event) {
        if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY)) {
            call(event, event.getCurrentItem());
        } else if(event.getAction().equals(InventoryAction.PLACE_ALL)
        || event.getAction().equals(InventoryAction.PLACE_ONE)
        || event.getAction().equals(InventoryAction.PLACE_SOME)) {
            call(event, event.getCursor());
        }

    }

    private void call(InventoryClickEvent event, ItemStack item) {
        eventService.callThrownEvent(
                (Player)event.getWhoClicked(),
                item,
                NoDropItemThrownEvent.Reason.MOVING_TO_OTHER_INVENTORY,
                Optional.of(event)
        );
    }
}
