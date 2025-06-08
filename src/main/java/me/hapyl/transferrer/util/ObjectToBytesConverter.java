package me.hapyl.transferrer.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Function;

public interface ObjectToBytesConverter<T> {
    
    byte[] asBytes(@Nonnull T t);
    
    @Nonnull
    T asObject(@Nonnull byte[] bytes);
    
    @Nullable
    default T asObjectOrNull(@Nonnull byte[] bytes) {
        try {
            return asObject(bytes);
        }
        catch (Exception e) {
            return null;
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
