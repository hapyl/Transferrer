package me.hapyl.transferrer.event;

import me.hapyl.transferrer.util.ServerId;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.player.PlayerEvent;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

public abstract class PlayerTransferEvent extends PlayerEvent {
    
    private final ServerId serverFromId;
    private final ServerId serverToId;
    
    @ApiStatus.Internal
    PlayerTransferEvent(@Nonnull Player player, @Nonnull ServerId serverFromId, @Nonnull ServerId serverToId) {
        super(player, true);
        
        this.serverFromId = serverFromId;
        this.serverToId = serverToId;
    }
    
    /**
     * Gets the {@link ServerId} of the server that transferring the player from.
     *
     * @return the {@link ServerId} of the server that transferring the player from.
     */
    @Nonnull
    public ServerId serverFromId() {
        return serverFromId;
    }
    
    /**
     * Gets the {@link ServerId} of the server that this player is being transferred to.
     *
     * @return the {@link ServerId} of the server that this player is being transferred to.
     */
    @Nonnull
    public ServerId serverToId() {
        return serverToId;
    }
    
    /**
     * Calls this event and returns {@code true} if it's {@link Cancellable} and {@link Cancellable#isCancelled()}, {@code false} otherwise.
     *
     * @return {@code true} if it's {@link Cancellable} and {@link Cancellable#isCancelled()}, {@code false} otherwise.
     */
    @Override
    public boolean callEvent() {
        Bukkit.getPluginManager().callEvent(this);
        
        if (this instanceof Cancellable cancellable) {
            return cancellable.isCancelled();
        }
        
        return false;
    }
}
