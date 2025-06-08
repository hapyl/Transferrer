package me.hapyl.transferrer.event;

import me.hapyl.transferrer.util.ServerId;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link PlayerTransferEvent} that is called whenever a player is being transferred from this server through the API.
 * <p>The event supports cancelling, preventing the player from being transferred.</p>
 *
 * <p><b>Transfer events are executed asynchronously!</b></p>
 */
public class PlayerTransferSendEvent extends PlayerTransferEvent implements Cancellable {
    
    private static final HandlerList handlerList = new HandlerList();
    
    private boolean cancel;
    
    @ApiStatus.Internal
    public PlayerTransferSendEvent(@NotNull Player player, @NotNull ServerId serverFromId, @NotNull ServerId serverToId) {
        super(player, serverFromId, serverToId);
    }
    
    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlerList;
    }
    
    /**
     * Gets whether the event is cancelled.
     * <p>Cancelling the event prevents the player from being transferred.</p>
     *
     * @return whether the event is cancelled.
     */
    @Override
    public boolean isCancelled() {
        return cancel;
    }
    
    /**
     * Sets whether the event is cancelled.
     * <p>Cancelling the event prevents the player from being transferred.</p>
     *
     * @param cancel {@code true} if you wish to cancel this event
     */
    @Override
    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
    
    @Nonnull
    public static HandlerList getHandlerList() {
        return handlerList;
    }
    
}
