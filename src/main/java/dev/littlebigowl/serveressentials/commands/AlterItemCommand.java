package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import dev.littlebigowl.serveressentials.events.LogFilter;
import net.md_5.bungee.api.ChatColor;

public class AlterItemCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(sender instanceof Player && label.equals("alteritem")) {

            Player player = (Player) sender;
            
            if(args[0].equals("name")) {

                StringBuilder name = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    name.append(args[i]).append(" ");
                }
                name.setLength(name.length() - 1);

                ItemStack item = player.getInventory().getItemInMainHand();
                if(item == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo item in your hand.")); return true; }
                ItemMeta itemMeta = item.getItemMeta();
                if(itemMeta == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo item in your hand.")); return true; }
                itemMeta.setDisplayName(ChatColor.translateAlternateColorCodes('&', "&r&f" + name.toString()));
                item.setItemMeta(itemMeta);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully altered item!"));

            } else if(args[0].equals("lore")) {

                StringBuilder name = new StringBuilder();
                for(int i = 1; i < args.length; i++) {
                    name.append(args[i]).append(" ");
                }
                name.setLength(name.length() - 1);

                List<String> lore = Arrays.asList(ChatColor.translateAlternateColorCodes('&', "&r&f" + name.toString().replaceAll(";", ";&r&f")).split(";"));

                ItemStack item = player.getInventory().getItemInMainHand();
                if(item == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo item in your hand.")); return true; }
                ItemMeta itemMeta = item.getItemMeta();
                if(itemMeta == null) { player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cNo item in your hand.")); return true; }
                itemMeta.setLore(lore);
                item.setItemMeta(itemMeta);

                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully altered item!"));
                
            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
            }

        }
        LogFilter.logCommand(sender, label, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 1) {
            ArrayList<String> possibleArgs = new ArrayList<>();
            possibleArgs.add("name");
            possibleArgs.add("lore");
            return possibleArgs;
        } else {
            return new ArrayList<>();
        }
    }
    
}
