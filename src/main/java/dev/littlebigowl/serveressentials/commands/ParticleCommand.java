package dev.littlebigowl.serveressentials.commands;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.events.LogFilter;
import net.md_5.bungee.api.ChatColor;

public class ParticleCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(label.equals("particle") && sender instanceof Player) {

            Player player = (Player) sender;
            if(args[0].equals("set")) {
                
                ArrayList<String> particles = new ArrayList<>();
                for(Particle particle : Particle.values()) { particles.add("minecraft:" + particle.toString()); }
                if(!particles.contains(args[1])) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis particle doesn't exist."));
                    return true;
                }

                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.createParticle(player.getUniqueId(), Particle.valueOf(args[1].replace("minecraft:", "")));
                } catch (Exception e) {
                    Bukkit.getLogger().info(e.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreated new particle effect."));

            } else if(args[0].equals("remove")) {

                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.deleteParticle(player.getUniqueId());
                } catch (SQLException e) {
                    Bukkit.getLogger().info(e.toString());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong."));
                    return true;
                }
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aDeleted particle effect."));

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
            possibleArgs.add("set");
            possibleArgs.add("remove");
            return possibleArgs;
        } else if(args.length == 2 && args[0].equals("set")) {
            ArrayList<String> stringParticles = new ArrayList<>();

            for(Particle particle : Particle.values()) {
                stringParticles.add("minecraft:" + particle.toString());
            }

            return StringUtil.copyPartialMatches(args[1], stringParticles, new ArrayList<>());
        } else {
            return new ArrayList<>();
        }
    }
    
}
