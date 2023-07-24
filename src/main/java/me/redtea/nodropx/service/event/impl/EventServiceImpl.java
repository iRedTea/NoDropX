package me.redtea.nodropx.service.event.impl;

import lombok.RequiredArgsConstructor;
import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.service.event.EventService;
import me.redtea.nodropx.service.nodrop.NoDropService;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

@RequiredArgsConstructor
public class EventServiceImpl implements EventService {
    private final NoDropService noDropService;

    @Override
    public void callThrownEvent(Player who, ItemStack item, NoDropItemThrownEvent.Reason reason, Optional<Cancellable> rootEvent) {
        if(!noDropService.isNoDrop(item)) return;
        NoDropItemThrownEvent callingEvent = new NoDropItemThrownEvent(
                who,
                reason,
                item
        );
        Bukkit.getPluginManager().callEvent(callingEvent);
        rootEvent.ifPresent(cancellable -> cancellable.setCancelled(callingEvent.isCancelled()));
    }
}
