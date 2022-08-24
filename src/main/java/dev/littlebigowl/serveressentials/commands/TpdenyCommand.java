package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpdenyCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("tpdeny")) {

            Player player = (Player) sender;
            Player targetPlayer = ServerEssentials.tpa.get(player);

            if (args.length != 0) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
                return true;
            }

            if (player == null || targetPlayer == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any teleportation requests."));
                return true;
            }

            ServerEssentials.tpa.remove(player);
            targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + player.getName() + "&e denied your teleportation request."));
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eDenied request from &6" + targetPlayer.getName()));

        } else if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is only usable by players."));
        }
        
        LogFilter.logCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
