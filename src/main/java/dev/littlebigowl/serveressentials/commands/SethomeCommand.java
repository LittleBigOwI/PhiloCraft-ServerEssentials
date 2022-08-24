package dev.littlebigowl.serveressentials.commands;

import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.utils.HomeUtil;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class SethomeCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("sethome")) {

            Player player = (Player) sender;
            ArrayList<String> playerHomeNames = HomeUtil.getHomeNames(player);
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
                            if(!(HomeUtil.getHomeCount(player) < 8)){
                                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any homes left."));
                                return true;
                            }

                            HomeUtil.createHome(player, args[0], player.getLocation());
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSet the home &6" + args[0]));
                        }
                        return true;

                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cHomes only work in the Overworld."));
                    }
            } else if(args.length == 2 && args[1].equals("override") && playerHomeNames.contains(args[0])) {
                HomeUtil.deleteHome(player, args[0]);
                HomeUtil.createHome(player, args[0], player.getLocation());
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aOverridden the home &6" + args[0]));

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
