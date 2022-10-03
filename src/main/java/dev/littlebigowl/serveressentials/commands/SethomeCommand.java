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
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SethomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("sethome")) {

            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = new ArrayList<>();
            try { playerHomeNames = ServerEssentials.database.cachedPlayerHomeNames.get(player.getUniqueId()); } catch (Exception e) {}
            
            if(args.length == 1) {
                if(player.getWorld().getEnvironment().equals(World.Environment.NORMAL)){

                    if(playerHomeNames.contains(args[0])) {

                        TextComponent context = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e&lHold on! &r&eYou are about to override another home. Proceed? "));
                        TextComponent accept = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&a✔&6] "));
                        TextComponent deny = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&c✘&6]"));

                        accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("accept")));
                        deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("deny")));

                        accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome " + args[0] + " override"));
                        deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/sethome " + args[0] + " revoke"));

                        ComponentBuilder finalMessage = new ComponentBuilder(context);
                        finalMessage.append(accept).append(deny);

                        player.spigot().sendMessage(finalMessage.create());
                    } else {
                        if(!(playerHomeNames.size() < 8)){
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any homes left."));
                            return true;
                        }
                        try {
                            ServerEssentials.database.resetConnection();
                            ServerEssentials.database.createHome(player.getUniqueId(), args[0], player.getLocation());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSet the home &6" + args[0]));
                        } catch (SQLException e) {
                            Bukkit.getLogger().info(e.toString());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                            return true;
                        }
                    }
                    return true;

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHomes only work in the Overworld."));
                }
            } else if(args.length == 2 && args[1].equals("override") && playerHomeNames.contains(args[0])) {
                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.deleteHome(player.getUniqueId(), args[0]);
                    ServerEssentials.database.createHome(player.getUniqueId(), args[0], player.getLocation());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOverridden the home &6" + args[0]));
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                    return true;
                }
            } else if (args.length == 2 && args[1].equals("revoke") && playerHomeNames.contains(args[0])) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cCanceled override."));

            } else {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
            }
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
