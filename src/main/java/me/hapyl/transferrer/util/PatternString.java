package me.hapyl.transferrer.util;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class PatternString {
    
    private final Pattern pattern;
    private final String string;
    
    public PatternString(@Nonnull Pattern pattern, @Nonnull String string) {
        if (!pattern.matcher(string).matches()) {
            throw new PatternSyntaxException("Malformed string for the pattern: ", pattern.toString(), -1);
        }
        
        this.pattern = pattern;
        this.string = string;
    }
    
    @Nonnull
    public Pattern pattern() {
        return pattern;
    }
    
    @Override
    public String toString() {
        return string;
    }
    
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        
        final PatternString that = (PatternString) o;
        return Objects.equals(this.string, that.string);
    }
    
    @Override
    public int hashCode() {
        return Objects.hashCode(this.string);
    }
}
