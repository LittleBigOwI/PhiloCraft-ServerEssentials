package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;

public class DelhomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("delhome")) {
            
            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId());
            
            if (args.length == 1) {
                if (playerHomeNames.contains(args[0])) {

                    TextComponent context = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e&lHold on! &r&eYou are about to remove &6" + args[0] + "&e. Proceed? "));
                    TextComponent accept = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&a✔&6] "));
                    TextComponent deny = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&c✘&6]"));

                    accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("accept")));
                    deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("deny")));

                    accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + args[0] + " confirm"));
                    deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/delhome " + args[0] + " refute"));

                    ComponentBuilder finalMessage = new ComponentBuilder(context);
                    finalMessage.append(accept).append(deny);

                    player.spigot().sendMessage(finalMessage.create());

                } else {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCould not find home."));
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("confirm")) {
                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.deleteHome(player.getUniqueId(), args[0]);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eRemoved &6" + args[0]));
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                    return true;
                }
            } else if (args.length == 2 && args[1].equalsIgnoreCase("refute")) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCanceled home removal."));
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
        if (sender instanceof Player && label.equalsIgnoreCase("delhome") && args.length == 1) {
            Player player = (Player) sender;
            
            return ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId());

        } else if (sender instanceof Player && label.equalsIgnoreCase("delhome") && args.length == 2) {
            ArrayList<String> options = new ArrayList<>();
            options.add("confirm");
            options.add("refute");
            return options;
        } else {
            ArrayList<String> options = new ArrayList<>();
            return options;
        }
    }
}
