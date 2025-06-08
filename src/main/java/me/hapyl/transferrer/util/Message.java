package me.hapyl.transferrer.util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@SuppressWarnings("deprecation")
public final class Message {
    
    private static final String prefix = "&8[&3Transferrer&8] ";
    
    public static void debug(Object obj) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + obj));
    }
    
    public static void send(CommandSender sender, Object obj) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', prefix + obj));
    }
    
    
}
