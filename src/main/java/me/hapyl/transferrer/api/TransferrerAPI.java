package me.hapyl.transferrer.api;

import me.hapyl.transferrer.config.TransferrerConfig;
import me.hapyl.transferrer.network.ServerIp;
import org.bukkit.entity.Player;

import javax.annotation.Nonnull;

/**
 * Represents the API.
 */
public interface TransferrerAPI {
    
    /**
     * Attempts to transfer the given {@link Player} to the given {@link ServerIp}.
     *
     * @param player   - The player to transfer.
     * @param serverIp - The ip to transfer to.
     */
    void transfer(@Nonnull Player player, @Nonnull ServerIp serverIp);
    
    /**
     * Gets the {@link TransferrerConfig} of this server.
     *
     * @return the {@link TransferrerConfig} of this server.
     */
    @Nonnull
    TransferrerConfig config();
    
}
