package me.redtea.nodropx.listener;

import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import me.redtea.nodropx.service.dropconfirm.impl.DropConfirmNull;
import me.redtea.nodropx.service.event.EventService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class InventoryHandler implements Listener {
    private final EventService eventService;
    private final NoDropService noDropService;
    private final DropConfirmService dropConfirmService;


    @EventHandler
    public void onMove(InventoryClickEvent event) {
        if(event.getAction().name().startsWith("DROP")
                && (noDropService.isNoDrop(event.getCursor()) || noDropService.isNoDrop(event.getCurrentItem()))
                && !(dropConfirmService instanceof DropConfirmNull)) {
            event.setCancelled(true);
            return;
        }

        if(event.getAction().equals(InventoryAction.MOVE_TO_OTHER_INVENTORY) && !event.getInventory().equals(event.getClickedInventory())) {
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
