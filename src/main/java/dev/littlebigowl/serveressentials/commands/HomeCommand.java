package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.models.Home;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import java.sql.SQLException;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && label.equalsIgnoreCase("home")) {
           
            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = new ArrayList<String>();
            try { playerHomeNames = ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId()); } catch (Exception e) {}
            
            if (args.length == 1) {
                if (playerHomeNames != null && playerHomeNames.contains(args[0])) {

                    if(!player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHomes only work in the Overworld."));
                        return true;
                    }
                    
                    try {
                        ServerEssentials.database.resetConnection();
                        Home playerHome = ServerEssentials.database.getHome(player.getUniqueId(), args[0]);
                        Location homeLoc = playerHome.getLocation();
                        player.teleport(homeLoc);
                    } catch (SQLException e) {
                        Bukkit.getLogger().info(e.toString());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                        return true;
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find home."));
                }
            } else if (args.length == 0) {
                if (playerHomeNames != null && playerHomeNames.contains("home")) {

                    Home playerHome;
                    try {
                        ServerEssentials.database.resetConnection();
                        playerHome = ServerEssentials.database.getHome(player.getUniqueId(), "home");
                        Location homeLoc = playerHome.getLocation();
                        player.teleport(homeLoc);
                    } catch (SQLException e) {
                        Bukkit.getLogger().info(e.toString());
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                        return true;
                    }

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find home."));
                }
            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
            }
        } else if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is only usable by players."));
        }
        
        LogFilter.logCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && label.equalsIgnoreCase("home") && args.length == 1) {
            Player player = (Player) sender;
            return ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId());
        }
        ArrayList<String> options = new ArrayList<>();
        return options;
    }
}
