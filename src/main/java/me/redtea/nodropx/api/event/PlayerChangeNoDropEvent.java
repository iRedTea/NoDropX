package me.redtea.nodropx.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when item changes no drop status
 */
public class PlayerChangeNoDropEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();


    private final boolean newValue;

    @Getter
    @NotNull
    private final ItemStack item;

    private boolean isCancelled;

    public PlayerChangeNoDropEvent(@NotNull Player who, boolean newValue, @NotNull ItemStack item) {
        super(who);
        this.newValue = newValue;
        this.item = item;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled ;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled  = cancel;
    }

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }

    public boolean getNewValue() {
        return newValue;
    }
}
