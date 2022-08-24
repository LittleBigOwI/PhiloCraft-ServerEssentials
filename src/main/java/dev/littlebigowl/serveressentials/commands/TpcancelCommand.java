package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpcancelCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("tpcancel")) {

            if (args.length != 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
                return true;
            }

            Player player = (Player) sender;
            Player targetPlayer = Bukkit.getPlayer(args[0]);


            if (player == null || targetPlayer == null || ServerEssentials.tpa.get(targetPlayer) == null) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any teleportation requests."));
                return true;
            }

            if (ServerEssentials.tpa.get(targetPlayer).getName().equals(player.getName())) { ServerEssentials.tpa.remove(targetPlayer); }
            if (!ServerEssentials.tpa.containsKey(targetPlayer)) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCanceled your request."));
                targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c" + player.getName() + " canceled his request."));
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have not sent any requests."));
            }

        } else if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is only usable by players."));
        }

        LogFilter.logCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) { return null; } else { return new ArrayList<>(); }
    }
}
