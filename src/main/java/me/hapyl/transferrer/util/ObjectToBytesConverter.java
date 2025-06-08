package me.hapyl.transferrer.util;

import me.hapyl.transferrer.exception.InvalidTransferPayloadException;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Function;

public interface ObjectToBytesConverter<T> {
    
    byte[] asBytes(@Nonnull T t);
    
    @Nonnull
    T asObject(@Nonnull byte[] bytes);
    
    @Nonnull
    default T asObject(@Nonnull byte[] bytes, @Nonnull InvalidTransferPayloadException.Cause cause) {
        try {
            return asObject(bytes);
        }
        catch (Exception e) {
            throw new InvalidTransferPayloadException(cause);
        }
    }
    
    @Nonnull
    static <T> ObjectToBytesConverter<T> of(@Nonnull Function<T, byte[]> asBytes, @Nonnull Function<byte[], T> asObject) {
        return new ObjectToBytesConverter<T>() {
            @Override
            public byte[] asBytes(@Nonnull T t) {
                return asBytes.apply(t);
            }
            
            @Override
            @Nonnull
            public T asObject(@Nonnull byte[] bytes) {
                return asObject.apply(bytes);
            }
        };
    }
    
}
