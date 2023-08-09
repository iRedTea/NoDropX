package me.redtea.nodropx.listener;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.menu.facade.MenuFacade;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

@RequiredArgsConstructor
public class ConfirmDropHandler implements Listener {
    private final DropConfirmService confirmService;
    private final MenuFacade menuFacade;

    @EventHandler
    public void onDrop(NoDropItemThrownEvent event) {
        if(!confirmService.isConfirmed(event.getPlayer()) && event.getReason() == NoDropItemThrownEvent.Reason.DROP_BY_PLAYER) {
            menuFacade.openConfirmDrop(event.getPlayer());
            event.setCancelled(true);
        } else confirmService.resetConfirm(event.getPlayer());
    }
}
