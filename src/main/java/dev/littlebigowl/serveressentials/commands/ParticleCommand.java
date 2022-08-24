package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Particle;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.models.PlayerParticle;
import dev.littlebigowl.serveressentials.utils.ParticleUtil;
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

                ParticleUtil.setParticle(player, Particle.valueOf(args[1].replace("minecraft:", "")));
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aCreated new particle effect."));

            } else if(args[0].equals("remove")) {

                PlayerParticle particle = ParticleUtil.deleteParticle(player);
                if(particle == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any particle effects."));
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

            return stringParticles;
        } else {
            return new ArrayList<>();
        }
    }
    
}
