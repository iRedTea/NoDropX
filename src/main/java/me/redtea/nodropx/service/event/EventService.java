package me.redtea.nodropx.service.event;

import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;

import java.util.Optional;

public interface EventService {
    void callThrownEvent(Player who, ItemStack item, NoDropItemThrownEvent.Reason reason, Optional<Cancellable> rootEvent);
}
