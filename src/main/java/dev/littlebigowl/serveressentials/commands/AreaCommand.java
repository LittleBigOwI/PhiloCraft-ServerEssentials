package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.math.Shape;
import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;
import dev.littlebigowl.serveressentials.utils.TeamUtil;
import net.md_5.bungee.api.ChatColor;


public class AreaCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equalsIgnoreCase("area")) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            
            if(args[0].equals("create") && args.length >= 2) {
                String name = "";
                for(int i = 1; i < args.length; i++) {
                    name = name + " " + args[i];
                }
                name = name.substring(1);
                
                ArrayList<Area> areas = ServerEssentials.database.playerAreas.get(playerUUID);
                if(areas == null) { ServerEssentials.database.playerAreas.put(playerUUID, new ArrayList<Area>()); }
                areas = ServerEssentials.database.playerAreas.get(playerUUID);

                for (Area area : areas) {
                    if(area.getName() == name) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou already have an area with the same name."));
                        return true;
                    }
                }

                if(areas.size() >= 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have reached the maximum amount of areas a player can have."));
                    return true;
                }

                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);

                Shape shape = new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z));
                Area area = new Area(name, playerUUID, shape);
                ServerEssentials.database.playerAreas.get(playerUUID).add(area);
                
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully created area"));
                area.draw(TeamUtil.getTeamColor(playtime));

            } else if(args[0].equals("expand") && args.length >= 2) {
                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                Shape shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)); //square
                
                String areaName = "";
                for(int i = 1; i < args.length; i++) {
                    areaName = areaName + " " + args[i];
                }
                areaName = areaName.substring(1);

                Area area = Area.getAreaByName(playerUUID, areaName);
                
                if(area != null && area.addChunk(shape)) {
                   player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded chunk to your area"));
                   int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                   area.draw(TeamUtil.getTeamColor(playtime));
                } else if(area != null){
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis chunk can't be added to your area."));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis area cannot be found."));
                }
            }
        }
    
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        if(sender instanceof Player) {
            player = (Player)sender;
        }
        if(label.equalsIgnoreCase("area") && args.length == 1) {
            return Arrays.asList("create", "expand", "edit", "delete", "subdue");
        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("expand")) {
            return ServerEssentials.database.getAreaNames(player.getUniqueId());
        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("edit")) {
            return Arrays.asList("name", "color", "groupName", "permissions");
        } else {
            return new ArrayList<>();
        }
    }
    
}
