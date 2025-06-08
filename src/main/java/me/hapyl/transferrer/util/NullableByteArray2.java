package me.hapyl.transferrer.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public interface NullableByteArray2 {
    
    @Nullable
    byte[] byte1();
    
    @Nullable
    byte[] byte2();
    
    @Nonnull
    static NullableByteArray2 of(@Nullable byte[] a, @Nullable byte[] b) {
        return new NullableByteArray2() {
            @Override
            @Nullable
            public byte[] byte1() {
                return a;
            }
            
            @Override
            @Nullable
            public byte[] byte2() {
                return b;
            }
        };
    }
    
}
