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
    SERVER_ID_MISMATCH,
    
    /**
     * This server does not allow transfers from the sending server.
     */
    SERVER_DID_NOT_ACCEPT,
    
    /**
     * The transfer was not made through the API.
     */
    NON_API_TRANSFER,
    
    /**
     * An unknown exception has occurred.
     */
    EXCEPTION;
    
    @Override
    @Nonnull
    public String label() {
        return name();
    }
}
