package dev.littlebigowl.serveressentials.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.ServerEssentials;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;

public class LinkCommand implements CommandExecutor, TabCompleter{

    private static String generateCode() {
        String upper = "BCDFGHJKLMNPQRSTVWXZ";
        String lower = "bcdfghjklmnpqrstvwxz";
        String number = "0123456789";
        
        int len = (int)(Math.random()*(12-8)) + 8;

        String code = "";

        for(int i = 0; i < len; i++) {
            int random = (int)(Math.random()*3);
            switch(random) {
                case 0 : code += String.valueOf(number.charAt((int)(number.length()*Math.random()))); break;
                case 1 : code += String.valueOf(lower.charAt((int)(lower.length()*Math.random()))); break;
                case 2 : code += String.valueOf(upper.charAt((int)(upper.length()*Math.random()))); break;
            }
        }

        return code;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if (sender instanceof Player && label.equalsIgnoreCase("link")) {

            Player player = (Player) sender;

            if(ServerEssentials.database.cachedPlayerDiscords.containsKey(player.getUniqueId())) {
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cAccount is already linked."));
                return true;
            }

            String code = generateCode();
            while(ServerEssentials.database.cachedPlayerCodes.containsValue(code)) {
                code = generateCode();
            }

            if(ServerEssentials.database.cachedPlayerCodes.containsKey(player.getUniqueId())) {
                code = ServerEssentials.database.cachedPlayerCodes.get(player.getUniqueId());
            } else {
                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.setupLink(player.getUniqueId(), code);
                } catch(SQLException e) {
                    Bukkit.getLogger().info(e.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                    return true;
                }
            }

            TextComponent startText = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&aYour link code is "));
            TextComponent codeText = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&6" + code + " &a"));
            TextComponent endText = new TextComponent(ChatColor.translateAlternateColorCodes('&', ". Go to the discord and enter your code using the /link command."));

            codeText.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Copy to chatbox")));
            codeText.setClickEvent(new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, code));

            ComponentBuilder finalMessage = new ComponentBuilder(startText)
                .append(codeText)
                .append(endText);

            player.spigot().sendMessage(finalMessage.create());
        
        } else if (sender instanceof BlockCommandSender || sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis command is only usable by players."));
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        return new ArrayList<>();
    }
}
