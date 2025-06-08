package me.hapyl.transferrer.exception;

import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class FixableIllegalArgumentException extends IllegalArgumentException {
    
    private final String fix;
    
    public FixableIllegalArgumentException(String s, String fix) {
        super(s);
        
        this.fix = fix;
    }
    
    public String fix() {
        return fix;
    }
}
