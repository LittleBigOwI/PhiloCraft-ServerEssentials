package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TpacceptCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("tpaccept")) {

            Player player = (Player) sender;
            Player targetPlayer = ServerEssentials.tpa.get(player);

            if (args.length != 1) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
                return true;
            }

            if (player == null || targetPlayer == null) {

                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any teleportation requests."));
                return true;
            }

            if(args[0].equals("here")) {
                targetPlayer.teleport(player.getLocation());
                ServerEssentials.tpa.remove(targetPlayer);
            } else if (args[0].equals("nothere")) {
                player.teleport(targetPlayer.getLocation());
                ServerEssentials.tpa.remove(targetPlayer);
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
                return true;
            }

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAccepted teleportation request from &6" + targetPlayer.getName()));
            targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6" + player.getName() + "&a has accepted your teleportation request"));

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
