package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.utils.TeamUtil;
import net.md_5.bungee.api.ChatColor;

public class RespondCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equals("r")) {
            
            if (args.length > 0) {
                
                Player player = (Player) sender;

                if (!MsgCommand.playerConv.containsKey(player)) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou dont have any messages."));
                    return true;
                }

                Player targetPlayer = MsgCommand.playerConv.get(player);

                String message = "";
                for (int i = 0; i < args.length; i++) {
                    message = message + args[i] + " ";
                }
                
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                int targetPlaytime = Math.round(targetPlayer.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);

                targetPlayer.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[" + TeamUtil.getTeamColor(playtime) + player.getName() + "&7 to " + " &cyou&7" + "] » &r" + message));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&7[" + "&cyou&7" + " &7to " + TeamUtil.getTeamColor(targetPlaytime) + targetPlayer.getName() + "] » &r" + message));
                
                Bukkit.getLogger().info("\u001b[38;5;248m[" + TeamUtil.getTerminalTeamColor(playtime) + player.getName() + "\u001b[38;5;248m to " + TeamUtil.getTerminalTeamColor(targetPlaytime) + targetPlayer.getName() + "\u001b[38;5;248m] » \u001b[37;1m" + message + "\u001b[0m");

            } else {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid Arguments."));
            }

        } else if (sender instanceof ConsoleCommandSender || sender instanceof BlockCommandSender) {
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
