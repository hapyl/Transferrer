package me.hapyl.transferrer.config;

import me.hapyl.transferrer.Transferrer;
import me.hapyl.transferrer.exception.FixableIllegalArgumentException;
import me.hapyl.transferrer.util.ServerId;
import me.hapyl.transferrer.util.Sha256;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;

@ApiStatus.Internal
public class TransferrerConfigImpl implements TransferrerConfig {
    
    private static final String acceptAllServersWildcard = "*";
    
    @Nonnull private final ServerId serverId;
    @Nonnull private final Sha256 secret;
    
    private final boolean hideJoinLeaveMessages;
    
    @Nullable private final List<ServerId> accepts; // Null means accept all
    
    public TransferrerConfigImpl(@Nonnull Transferrer main) {
        main.reloadConfig();
        
        final FileConfiguration config = main.getConfig();
        main.saveDefaultConfig();
        
        this.serverId = fetchConfigValue(
                config, "server_id", FileConfiguration::getString, id -> {
                    try {
                        return new ServerId(id);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        throw main.severeShutdown(() -> new FixableIllegalArgumentException(
                                "Malformed 'server_id'!",
                                "Make sure the id matches the following pattern: %s!".formatted(ServerId.serverIdPattern.toString())
                        ));
                    }
                }
        );
        
        this.secret = fetchConfigValue(
                config, "secret_sha256", FileConfiguration::getString, secret -> {
                    try {
                        return Sha256.fromString(secret);
                    }
                    catch (IllegalArgumentException illegalArgumentException) {
                        throw main.severeShutdown(() -> new FixableIllegalArgumentException(
                                "Malformed 'secret_sha256'!",
                                "Make sure the secret is a valid Sha256 string!"
                        ));
                    }
                }
        );
        
        this.hideJoinLeaveMessages = fetchConfigValue(config, "hide_join_leave_messages", FileConfiguration::getBoolean, Function.identity());
        
        this.accepts = fetchConfigValue(
                config, "accepts", FileConfiguration::getStringList, accepts -> {
                    if (accepts.contains(acceptAllServersWildcard)) {
                        return null; // null means we accept any servers
                    }
                    
                    return accepts.stream()
                                  .map(ServerId::new)
                                  .toList();
                }
        );
    }
    
    @Nonnull
    @Override
    public ServerId serverId() {
        return serverId;
    }
    
    @Override
    public boolean hideJoinLeaveMessages() {
        return hideJoinLeaveMessages;
    }
    
    @Nonnull
    public Sha256 secret() {
        return secret;
    }
    
    @Override
    public boolean acceptsAny() {
        return accepts == null;
    }
    
    @Override
    public boolean accepts(@Nonnull ServerId id) {
        return accepts == null || accepts.contains(id);
    }
    
    private static <S, T> T fetchConfigValue(FileConfiguration config, String key, BiFunction<FileConfiguration, String, S> getFn, Function<S, T> fn) {
        final S s = getFn.apply(config, key);
        
        return fn.apply(s);
    }
}
