package me.redtea.nodropx.api.event;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when player that has no drop item dead
 */
public class NoDropItemDropOnDeathEvent extends PlayerEvent {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    @NotNull
    private final ItemStack item;

    @Getter
    @Setter
    private boolean willDropped = false;

    public NoDropItemDropOnDeathEvent(Player who, @NotNull ItemStack item) {
        super(who);
        this.item = item;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

}
