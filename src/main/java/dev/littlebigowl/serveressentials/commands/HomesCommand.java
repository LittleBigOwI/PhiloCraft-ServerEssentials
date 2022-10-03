package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;

import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class HomesCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("homes")) {
            
            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = new ArrayList<String>();
            try { playerHomeNames = ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId()); } catch (Exception e) {}

            if(playerHomeNames == null) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aYou don't have any homes."));
                return true;
            }

            StringBuffer homelist = new StringBuffer();
            homelist.append(ChatColor.translateAlternateColorCodes('&', "&eHomes&6[&e" + Integer.toString(playerHomeNames.size()) + "&6]&7: &6"));

            int i = 0;
            for(String homeName : playerHomeNames) {
                if(i%2 == 0) {
                    homelist.append(homeName).append(ChatColor.translateAlternateColorCodes('&', " &7| &e"));
                } else {
                    homelist.append(homeName).append(ChatColor.translateAlternateColorCodes('&', " &7| &6"));
                }
                i++;
            }
            homelist.delete(homelist.length()-7, homelist.length());
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', homelist.toString()));

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
