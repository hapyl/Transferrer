package me.hapyl.transferrer.event;

import me.hapyl.transferrer.util.ServerId;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

/**
 * Represents a {@link PlayerTransferEvent} that is called whenever a player has been transferred through the API.
 * <p><b>Transfer events are executed asynchronously!</b></p>
 */
public class PlayerTransferReceiveEvent extends PlayerTransferEvent {
    
    private static final HandlerList handlerList = new HandlerList();
    
    @ApiStatus.Internal
    public PlayerTransferReceiveEvent(@NotNull Player player, @NotNull ServerId serverFromId, @NotNull ServerId serverToId) {
        super(player, serverFromId, serverToId);
    }
    
    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return handlerList;
    }
    
    @Nonnull
    public static HandlerList getHandlerList() {
        return handlerList;
    }
}
