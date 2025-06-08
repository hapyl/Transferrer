package me.hapyl.transferrer;

import me.hapyl.transferrer.api.TransferrerAPI;
import me.hapyl.transferrer.config.TransferrerConfigImpl;
import me.hapyl.transferrer.exception.FixableIllegalArgumentException;
import me.hapyl.transferrer.network.TransferHandler;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.logging.Logger;

public final class Transferrer extends JavaPlugin {
    
    private static Transferrer instance;
    
    private TransferrerConfigImpl config;
    private TransferHandler handler;
    
    @Override
    public void onEnable() {
        instance = this;
        
        // Check if 'accepts-transfers' is set to 'true'
        if (!getServer().isAcceptingTransfers()) {
            throw severeShutdown(() -> new FixableIllegalArgumentException(
                    "Server doesn't accept transfers!",
                    "Set 'accepts-transfers' to 'true' in server.properties!"
            ));
        }
        
        // Initiate the config
        config = new TransferrerConfigImpl(this);
        
        // Initiate the handler
        handler = registerEvent(() -> new TransferHandler(this));
        
        // Register command
        Bukkit.getCommandMap().register("transferrer", new TransferrerCommand(this, "transferrer"));
    }
    
    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    
    @Nonnull
    public TransferrerConfigImpl config() {
        return Objects.requireNonNull(config);
    }
    
    public <X extends FixableIllegalArgumentException> X severeShutdown(@Nonnull Supplier<X> supplier) throws X {
        final Logger logger = getLogger();
        final X x = supplier.get();
        
        logger.severe("------------------------------------------");
        logger.severe("An exception has occurred!");
        logger.severe(" REASON: " + x.getMessage());
        logger.severe(" FIX:    " + x.fix());
        logger.severe("------------------------------------------");
        
        Bukkit.getPluginManager().disablePlugin(this);
        
        return x;
    }
    
    public void doReloadConfig() {
        config = new TransferrerConfigImpl(this);
    }
    
    private <T extends Listener> T registerEvent(Supplier<T> t) {
        final T listener = t.get();
        Bukkit.getPluginManager().registerEvents(listener, this);
        
        return listener;
    }
    
    /**
     * Gets the {@link TransferrerAPI} implementation singleton.
     *
     * @return the {@link TransferrerAPI} implementation singleton.
     */
    @Nonnull
    public static TransferrerAPI getAPI() {
        return instance.handler;
    }
    
}
