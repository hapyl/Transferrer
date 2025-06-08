package me.hapyl.transferrer.util;

import javax.annotation.Nonnull;

/**
 * Represents a labelled object.
 */
public interface Labelled {
    
    /**
     * Gets the label of the object.
     *
     * @return the label of the object.
     */
    @Nonnull
    String label();
    
}
