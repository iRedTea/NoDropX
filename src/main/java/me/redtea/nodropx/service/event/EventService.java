package me.redtea.nodropx.service.event;

import me.redtea.nodropx.api.event.NoDropItemThrownEvent;
import me.redtea.nodropx.api.event.PlayerChangeNoDropEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public interface EventService {
    PlayerChangeNoDropEvent callPlayerChangeNoDropEvent(Player who, ItemStack item, boolean value);
    @Nullable
    NoDropItemThrownEvent callThrownEvent(Player who, ItemStack item, NoDropItemThrownEvent.Reason reason, Optional<Cancellable> rootEvent);
}
