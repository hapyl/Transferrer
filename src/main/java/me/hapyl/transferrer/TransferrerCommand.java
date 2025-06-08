package me.hapyl.transferrer;

import me.hapyl.transferrer.network.ServerIp;
import me.hapyl.transferrer.util.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.util.NumberConversions;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;

public class TransferrerCommand extends Command {
    
    private static final Permission permission = new Permission("transferrer.command");
    
    private final Transferrer transferrer;
    
    TransferrerCommand(@Nonnull Transferrer transferrer, @NotNull String name) {
        super(name);
        
        this.transferrer = transferrer;
    }
    
    @Override
    public boolean execute(@NotNull CommandSender sender, @NotNull String commandLabel, @NotNull String @NotNull [] args) {
        // command <reload>                       - Reloads config
        // command <goto> <server_id> <ip> <port> - Transfer to the server
        if (!(sender instanceof Player player)) {
            return true;
        }
        
        if (!player.hasPermission(permission)) {
            Message.send(player, "&4No permissions.");
            return true;
        }
        
        // If no arguments, display the current server
        if (args.length < 1) {
            Message.send(player, "&aYou're on server &2'%s'&a!".formatted(transferrer.config().serverId()));
            return true;
        }
        
        final String arg0 = args[0].toLowerCase();
        
        switch (arg0) {
            case "reload" -> {
                try {
                    transferrer.doReloadConfig();
                    
                    Message.send(player, "&2Config was successfully reloaded!");
                }
                catch (Exception e) {
                    e.printStackTrace();
                    Message.send(player, "&4There was an exception reloading the config: %s.".formatted(e.getMessage()));
                }
            }
            
            case "goto" -> {
                final String serverId = argumentValue(args, 1, Function.identity());
                final String hostName = argumentValue(args, 2, Function.identity());
                final Integer port = argumentValue(args, 3, NumberConversions::toInt);
                
                if (serverId == null) {
                    Message.send(player, "&4Invalid server_id!");
                    return true;
                }
                
                if (hostName == null) {
                    Message.send(player, "&4Invalid host!");
                    return true;
                }
                
                if (port == null || ServerIp.isPortOutsideBounds(port)) {
                    Message.send(player, "&4Invalid or out of bounds port!");
                    return true;
                }
                
                Message.send(player, "&eAttempting to transfer you to '%s'...".formatted(serverId));
                
                // Transfer using API
                try {
                    Transferrer.getAPI().transfer(player, ServerIp.of(serverId, hostName, port));
                } catch (Exception e) {
                    Message.send(player, "&4Error! &c" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        
        return false;
    }
    
    @Override
    public @NotNull List<String> tabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String @NotNull [] args) throws IllegalArgumentException {
        if (args.length == 1) {
            return sortedArgumentList(args, "reload", "goto");
        }
        
        return List.of();
    }
    
    private List<String> sortedArgumentList(String[] args, String... values) {
        if (args.length == 0) {
            return List.of();
        }
        
        final String latest = args[args.length - 1].toLowerCase();
        
        return Arrays.stream(values)
                     .filter(arg -> arg.contains(latest))
                     .toList();
    }
    
    @Nullable
    private <T> T argumentValue(String[] args, int index, Function<String, T> func) {
        if (index < 0 || index >= args.length) {
            return null;
        }
        
        return func.apply(args[index]);
    }
    
}
