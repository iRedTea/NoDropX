package me.redtea.nodropx.api.event;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerEvent;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * This event is called when no drop item is moved from the player's inventory
 */
public class NoDropItemThrownEvent extends PlayerEvent implements Cancellable {
    private static final HandlerList handlers = new HandlerList();

    @Getter
    @NotNull
    private final Reason reason;

    @Getter
    @NotNull
    private final ItemStack item;

    private boolean isCancelled;

    public NoDropItemThrownEvent(Player who, @NotNull Reason reason, @NotNull ItemStack item) {
        super(who);
        this.reason = reason;
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

    public enum Reason {
        DROP_BY_PLAYER, MOVING_TO_OTHER_INVENTORY
    }
}
