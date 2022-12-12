package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Statistic;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.events.LogFilter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class RankinfoCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equals("rankinfo")) {
            
            if (args.length == 0) {
                
                Player player = (Player) sender;
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);

                String greenOrRed = "";
                Integer percent = 0;

        
                TextComponent top = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&e==================== &6&lRankInfo&r&e ===================="));
                TextComponent playtimeSeparator = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&7&oHover on time to view progress\n\n&r&f&lPlaytime Ranks:&r"));
                TextComponent staffSeparator = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n\n&f&lStaff Ranks:\n&r&7[&cADMIN&7]&r"));
                TextComponent end = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&e==================================================&r"));

                TextComponent guestInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&7&oGuest - The default rank&r"));
                
                if(playtime < 300) { greenOrRed = "&c"; percent = Math.round((playtime*100)/300); } else { greenOrRed = "&a"; percent = 100;}
                TextComponent playerInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&3[Player] - "));
                TextComponent playerTimeInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', greenOrRed + "[5 hours]&r"));
                playerTimeInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "You have &b" + playtime/60 + "&r hours | " + percent + "%"))));
                
                if(playtime < 1500) { greenOrRed = "&c"; percent = Math.round((playtime*100)/1500); } else { greenOrRed = "&a"; percent = 100;}
                TextComponent playerPlusInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&3[Player+] - "));
                TextComponent playerPlusTimeInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', greenOrRed + "[25 hours]&r"));
                playerPlusTimeInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "You have &b" + playtime/60 + "&r hours | " + percent + "%"))));
                
                if(playtime < 5000) { greenOrRed = "&c"; percent = Math.round((playtime*100)/5000); } else { greenOrRed = "&a"; percent = 100;}
                TextComponent memberInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&b[Member] - "));
                TextComponent memberTimeInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', greenOrRed + "[83 hours]&r"));
                memberTimeInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "You have &b" + playtime/60 + "&r hours | " + percent + "%"))));
                
                if(playtime < 10000) { greenOrRed = "&c"; percent = Math.round((playtime*100)/10000); } else { greenOrRed = "&a"; percent = 100;}
                TextComponent pcInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&6[PhiloCrafter] - "));
                TextComponent pcTimeInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', greenOrRed + "[167 hours]&r"));
                pcTimeInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "You have &b" + playtime/60 + "&r hours | " + percent + "%"))));

                if(playtime < 30000) { greenOrRed = "&c"; percent = Math.round((playtime*100)/30000); } else { greenOrRed = "&a"; percent = 100;}
                TextComponent pcPlusInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&6[PhiloCrafter+] - "));
                TextComponent pcPlusTimeInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', greenOrRed + "[500 hours]&r"));
                pcPlusTimeInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "You have &b" + playtime/60 + "&r hours | " + percent + "%"))));
                
                top.addExtra(playtimeSeparator);
                top.addExtra(guestInfo);
                top.addExtra(playerInfo);
                top.addExtra(playerTimeInfo);
                top.addExtra(playerPlusInfo);
                top.addExtra(playerPlusTimeInfo);
                top.addExtra(memberInfo);
                top.addExtra(memberTimeInfo);
                top.addExtra(pcInfo);
                top.addExtra(pcTimeInfo);
                top.addExtra(pcPlusInfo);
                top.addExtra(pcPlusTimeInfo);
                top.addExtra(staffSeparator);
                top.addExtra(end);

                player.spigot().sendMessage(top);
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
