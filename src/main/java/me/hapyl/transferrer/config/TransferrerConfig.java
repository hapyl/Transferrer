package me.hapyl.transferrer.config;

import me.hapyl.transferrer.util.ServerId;

import javax.annotation.Nonnull;

/**
 * Represents a config for the plugin.
 */
public interface TransferrerConfig {
    
    /**
     * Gets the {@link ServerId} of this server.
     *
     * @return the {@link ServerId} of this server.
     */
    @Nonnull
    ServerId serverId();
    
    /**
     * Gets whether this server accepts any transfers.
     *
     * @return {@code true} if this server accepts any transfers, {@code false} if this server can only transfer.
     */
    boolean acceptsAny();
    
    /**
     * Gets whether this sever accepts transfers from the given {@link ServerId}.
     *
     * @param id - The id to check.
     * @return {@code true} if this server accepts transfers from the given {@link ServerId}.
     */
    boolean accepts(@Nonnull ServerId id);
    
    /**
     * Gets whether join and quit messages are hidden upon transferring.
     *
     * @return whether join and quit messages are hidden upon transferring.
     */
    boolean hideJoinLeaveMessages();
}
