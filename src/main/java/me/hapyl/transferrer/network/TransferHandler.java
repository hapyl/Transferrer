package me.hapyl.transferrer.network;

import me.hapyl.transferrer.Transferrer;
import me.hapyl.transferrer.api.TransferrerAPI;
import me.hapyl.transferrer.config.TransferrerConfig;
import me.hapyl.transferrer.config.TransferrerConfigImpl;
import me.hapyl.transferrer.event.PlayerTransferReceiveEvent;
import me.hapyl.transferrer.event.PlayerTransferSendEvent;
import me.hapyl.transferrer.exception.InvalidTransferPayloadException;
import me.hapyl.transferrer.util.Labelled;
import me.hapyl.transferrer.util.Message;
import me.hapyl.transferrer.util.ServerId;
import me.hapyl.transferrer.util.Sha256;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.jetbrains.annotations.ApiStatus;

import javax.annotation.Nonnull;
import java.net.InetSocketAddress;

@ApiStatus.Internal
public final class TransferHandler implements TransferrerAPI, Listener {
    
    private final Transferrer transferrer;
    
    public TransferHandler(@Nonnull Transferrer transferrer) {
        this.transferrer = transferrer;
    }
    
    @Override
    public void transfer(@Nonnull Player player, @Nonnull ServerIp serverIp) {
        // Don't transfer to the same server
        final Server server = Bukkit.getServer();
        
        final InetSocketAddress address = serverIp.address();
        final InetSocketAddress currentAddress = server.getIp().isEmpty()
                                                 ? new InetSocketAddress("localhost", server.getPort())
                                                 : new InetSocketAddress(server.getIp(), server.getPort());
        
        // Can't transfer to the same server
        if (address.equals(currentAddress)) {
            Message.send(player, "&4Cannot transfer player to the same server!");
            return;
        }
        
        async(() -> {
            if (serverIp.isLocalhost() && !serverIp.isLocalhostBound()) {
                Message.send(player, "&4Sever '%s' is offline!".formatted(serverIp.serverId()));
                return;
            }
            
            // Prepare payload
            final TransferPayload payload = new TransferPayload(transferrer.config().serverId(), serverIp.serverId(), transferrer.config().secret());
            
            // Call event and check for cancel
            if (new PlayerTransferSendEvent(player, payload.serverFromId(), payload.serverToId()).callEvent()) {
                return;
            }
            
            // Store cookies
            payload.store(player);
            
            // Transfer
            
            player.transfer(address.getHostName(), address.getPort());
        });
    }
    
    @Override
    @Nonnull
    public TransferrerConfig config() {
        return transferrer.config();
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerJoinEvent(PlayerJoinEvent ev) {
        final Player player = ev.getPlayer();
        
        if (!player.isTransferred()) {
            return;
        }
        
        final TransferrerConfigImpl config = transferrer.config();
        
        if (config.hideJoinLeaveMessages()) {
            ev.joinMessage(null);
        }
        
        TransferPayload.retrieve(player)
                       .thenAcceptAsync(payload -> {
                           if (payload == null) {
                               return; // Another plugin transfer
                           }
                           
                           // Validate server_to is this server
                           final ServerId serverTo = payload.serverToId();
                           final ServerId serverFrom = payload.serverFromId();
                           final Sha256 secret = payload.secret();
                           
                           if (!config.serverId().equals(serverTo)) {
                               kickInvalidPayload(player, KickReason.WRONG_SERVER_TO);
                               return;
                           }
                           
                           // Validate server is acceptable
                           if (!config.accepts(serverFrom)) {
                               kickInvalidPayload(player, KickReason.DOES_NOT_ACCEPT_SERVER);
                               return;
                           }
                           
                           // Validate secret
                           if (!config.secret().equals(secret)) {
                               kickInvalidPayload(player, KickReason.INVALID_SECRET);
                               return;
                           }
                           
                           // Ok transfer, call event
                           new PlayerTransferReceiveEvent(player, serverFrom, serverTo).callEvent();
                       })
                       .exceptionally(ex -> {
                           if (!(ex.getCause() instanceof InvalidTransferPayloadException exception)) {
                               return null;
                           }
                           
                           final InvalidTransferPayloadException.Cause cause = exception.cause();
                           
                           // When exception is thrown, it's probably due to malformed or illegal cookies,
                           // meaning it's most likely not plugin's fault, so log it ig
                           transferrer.getLogger().warning(
                                   """
                                   Kicked %s because of invalid transfer payload: %s.
                                   
                                   It's likely that it isn't a plugin-related issue and player is trying to 'hack' the server.""".formatted(
                                           player.getName(),
                                           cause
                                   ));
                           
                           
                           kickInvalidPayload(player, cause);
                           return null;
                       });
    }
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void handlePlayerQuitEvent(PlayerQuitEvent ev) {
        final Player player = ev.getPlayer();
        
        if (!player.isTransferred()) {
            return;
        }
        
        if (transferrer.config().hideJoinLeaveMessages()) {
            ev.quitMessage(null);
        }
    }
    
    private void sync(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTask(transferrer);
    }
    
    private void async(Runnable runnable) {
        new BukkitRunnable() {
            @Override
            public void run() {
                runnable.run();
            }
        }.runTaskAsynchronously(transferrer);
    }
    
    private <E extends Enum<E> & Labelled> void kickInvalidPayload(Player player, E invalidSecret) {
        sync(() -> player.kick(
                Component.text()
                         .append(Component.text("Illegal transfer!", NamedTextColor.DARK_RED, TextDecoration.BOLD))
                         .appendNewline()
                         .appendNewline()
                         .append(Component.text("An error has occurred during transfer:", NamedTextColor.RED))
                         .appendNewline()
                         .append(Component.text(invalidSecret.label(), NamedTextColor.WHITE))
                         .build(),
                PlayerKickEvent.Cause.INVALID_COOKIE
        ));
    }
    
}
