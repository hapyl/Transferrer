package me.hapyl.transferrer.exception;

import me.hapyl.transferrer.util.Labelled;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;

@ApiStatus.Internal
public class InvalidTransferPayloadException extends RuntimeException {
    
    private final Cause cause;
    
    public InvalidTransferPayloadException(Cause cause) {
        super();
        this.cause = cause;
    }
    
    @Nonnull
    public Cause cause() {
        return cause;
    }
    
    /**
     * Denotes the cause of the exception.
     */
    public enum Cause implements Labelled {
        /**
         * Denotes that the 'server_from' cookie is missing.
         */
        ILLEGAL_SERVER_FROM,
        
        /**
         * Denotes that 'server_to' cookie is missing.
         */
        ILLEGAL_SERVER_TO,
        
        /**
         * Denotes that 'server_from' cookie is malformed.
         */
        MALFORMED_SERVER_FROM,
        
        /**
         * Denotes that 'server_to' cookie if malformed.
         */
        MALFORMED_SERVER_TO,
        
        /**
         * Denotes that 'secret' cookie is malformed.
         */
        MALFORMED_SECRET;
        
        @Override
        @Nonnull
        public String label() {
            return name();
        }
    }
    
}
