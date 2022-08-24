package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.utils.HomeUtil;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class HomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player && label.equalsIgnoreCase("home")) {
            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = HomeUtil.getHomeNames(player);

            if (args.length == 1) {
                if (playerHomeNames.contains(args[0])) {

                    if(!player.getWorld().getEnvironment().equals(World.Environment.NORMAL)) {
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHomes only work in the Overworld."));
                        return true;
                    }

                    Location homeLoc = HomeUtil.getHomeLocation(Objects.requireNonNull(HomeUtil.getHome(player, args[0])));
                    player.teleport(homeLoc);

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find home."));
                }
            } else if (args.length == 0) {
                if (playerHomeNames.contains("home")) {

                    Location homeLoc = HomeUtil.getHomeLocation(Objects.requireNonNull(HomeUtil.getHome(player, "home")));
                    player.teleport(homeLoc);

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
            return HomeUtil.getHomeNames(player);
        }
        ArrayList<String> options = new ArrayList<>();
        return options;
    }
}
