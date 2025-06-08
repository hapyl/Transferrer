package me.hapyl.transferrer.util;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Base64;
import java.util.Objects;

/**
 * Represents a Sha256 hash encoded as a byte array.
 */
public final class Sha256 {
    
    private final byte[] secretBytes;
    
    private Sha256(byte[] secretBytes) {
        this.secretBytes = secretBytes;
    }
    
    @Override
    public String toString() {
        return Base64.getEncoder().encodeToString(secretBytes);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final Sha256 that = (Sha256) o;
        return Objects.deepEquals(this.secretBytes, that.secretBytes);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.secretBytes);
    }
    
    public byte[] encoded() {
        return Base64.getEncoder().encode(secretBytes);
    }
    
    @Nonnull
    public static Sha256 fromString(@Nonnull String base64) {
        if (base64.isEmpty()) {
            throw new IllegalArgumentException("The base64 must not be empty!");
        }
        
        return new Sha256(Base64.getDecoder().decode(base64));
    }
    
}
