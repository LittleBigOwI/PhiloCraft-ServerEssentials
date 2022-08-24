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
import org.bukkit.scheduler.BukkitScheduler;

import java.util.ArrayList;
import java.util.List;

public class TpaCommand implements CommandExecutor, TabCompleter {

    private final ServerEssentials testPlugin;
    public TpaCommand(ServerEssentials testPlugin) {
        this.testPlugin = testPlugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player && label.equalsIgnoreCase("tpa")) {
            if (args.length == 1) {
                Player player = (Player) sender;
                Player targetPlayer = Bukkit.getPlayer(args[0]);

                if (targetPlayer == null) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cPlayer was not found."));
                    return true;
                }
                if (player.getName().equals(targetPlayer.getName())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat seems a bit useless."));
                    return true;
                }
                if(ServerEssentials.tpa.containsKey(targetPlayer)) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis player already has a teleportation request."));
                    return true;
                }
                if(!player.getWorld().getEnvironment().name().equals(targetPlayer.getWorld().getEnvironment().name())) {
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou and " + targetPlayer.getName() + " are not in the same world."));
                    return true;
                }

                ServerEssentials.tpa.put(targetPlayer, player);

                TextComponent context = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6" + player.getName() + "&e wants to teleport to you "));
                TextComponent accept = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&a✔&6] "));
                TextComponent deny = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6[&c✘&6]"));

                TextComponent info = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&eRequest was sent to " + targetPlayer.getName() + " "));
                TextComponent cancel = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e[&c✘&e]&f"));

                accept.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept here"));
                deny.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpdeny"));
                cancel.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpcancel " + targetPlayer.getName()));

                accept.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("accept")));
                deny.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("deny")));
                cancel.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("cancel")));

                ComponentBuilder finalMessage = new ComponentBuilder(context);
                finalMessage.append(accept);
                finalMessage.append(deny);

                ComponentBuilder finalCancelMessage = new ComponentBuilder(info);
                finalCancelMessage.append(cancel);

                targetPlayer.spigot().sendMessage(finalMessage.create());
                player.spigot().sendMessage(finalCancelMessage.create());

                BukkitScheduler scheduler = Bukkit.getScheduler();
                scheduler.runTaskLater(this.testPlugin, () -> {
                    if(ServerEssentials.tpa.containsKey(targetPlayer)) {
                        ServerEssentials.tpa.remove(targetPlayer);
                    }
                }, 400);

                return true;

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid arguments."));
            }
        } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
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
