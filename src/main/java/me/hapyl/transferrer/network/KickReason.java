package me.hapyl.transferrer.network;

import me.hapyl.transferrer.util.Labelled;

import javax.annotation.Nonnull;

/**
 * Represents a kick reason that has occurred when receiving a player transfer.
 */
public enum KickReason implements Labelled {
    /**
     * The 'secret' from sending server does not match this server's secret.
     */
    INVALID_SECRET,
    
    /**
     * The 'server_id' from sending server does not match this server's id.
     */
    WRONG_SERVER_TO,
    
    /**
     * This server does not allow transfers from the sending server.
     */
    DOES_NOT_ACCEPT_SERVER;
    
    @Override
    @Nonnull
    public String label() {
        return name();
    }
}
