package me.redtea.nodropx.listener;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.menu.facade.MenuFacade;
import me.redtea.nodropx.service.dropconfirm.DropConfirmService;
import me.redtea.nodropx.service.event.EventService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import me.redtea.nodropx.service.respawn.RespawnService;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@RequiredArgsConstructor
public class PlayerHandler implements Listener {
    private final RespawnService respawnService;
    private final EventService eventService;
    private final MenuFacade menuFacade;

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        eventService.callThrownEvent(
                event.getPlayer(),
                event.getItemDrop().getItemStack(),
                NoDropItemThrownEvent.Reason.DROP_BY_PLAYER,
                Optional.of(event)
        );
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDeath(PlayerDeathEvent event) {
        respawnService.saveItemsBeforeDeath(event);
    }

    @EventHandler
    public void onRespawn(PlayerRespawnEvent event) {
        respawnService.giveSavedItems(event.getPlayer());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        respawnService.giveSavedItems(event.getPlayer());
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        menuFacade.clearCache(event.getPlayer());
    }

}
