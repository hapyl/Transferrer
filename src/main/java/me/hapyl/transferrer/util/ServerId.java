package me.hapyl.transferrer.util;

import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * Represents a server id; all ids must match the {@link #serverIdPattern}!
 */
public class ServerId extends PatternString {
    
    public static final Pattern serverIdPattern = Pattern.compile("^[a-z0-9_]+$");
    
    public ServerId(@NotNull String string) {
        super(serverIdPattern, string);
    }
    
}
