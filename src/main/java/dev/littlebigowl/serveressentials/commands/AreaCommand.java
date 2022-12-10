package dev.littlebigowl.serveressentials.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.math.Shape;
import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;
import net.md_5.bungee.api.ChatColor;


public class AreaCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equalsIgnoreCase("area")) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            
            if(args[0].equals("create") && args.length == 2) {
                String name = args[1];
                
                ArrayList<Area> areas = ServerEssentials.database.playerAreas.get(playerUUID);
                if(areas == null) { ServerEssentials.database.playerAreas.put(playerUUID, new ArrayList<Area>()); }
                areas = ServerEssentials.database.playerAreas.get(playerUUID);

                for (Area area : areas) {
                    if(area.getName() == name) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou already have a town with the same name."));
                        return true;
                    }
                }

                if(areas.size() >= 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have reached the maximum amount of towns a player can have."));
                    return true;
                }

                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                
                Shape shape = null;
                if(name.equals("sq")) {
                    shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)); //square
                } else if(name.equals("l")) {
                    shape = new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x, z+32), new Vector2d(x+16, z+32), new Vector2d(x+32, z+32), new Vector2d(x+32, z+16), new Vector2d(x+16, x+16), new Vector2d(x+16, z));
                } else if(name.equals("l2")) {
                    shape = new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x, z+32), new Vector2d(x-16, z+32), new Vector2d(x-32, z+32), new Vector2d(x-32, z+16), new Vector2d(x-16, z+16), new Vector2d(x-16, z));
                } else if(name.equals("u")) {
                    shape = new Shape(
                    new Vector2d(x, z),
                    new Vector2d(x, z+16),
                    new Vector2d(x, z+32),
                    new Vector2d(x+16, z+32),
                    new Vector2d(x+32, z+32),
                    new Vector2d(x+48, z+32),
                    new Vector2d(x+48, z+16),
                    new Vector2d(x+48, z),
                    new Vector2d(x+32, z),
                    new Vector2d(x+32, z+16),
                    new Vector2d(x+16, z+16),
                    new Vector2d(x+16, z));
                }
                Area area = new Area(name, playerUUID, shape);
                ServerEssentials.database.playerAreas.get(playerUUID).add(area);
            
                area.create(player);
            } else if(args[0].equals("expand") && args.length == 1) {
                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk

                Shape shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)); //square
                
                ArrayList<Area> areas = ServerEssentials.database.playerAreas.get(playerUUID);
                
                areas.get(0).expand(shape);
            }
        }
    
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if(label.equalsIgnoreCase("area") && args.length == 1) {
            return Arrays.asList("create", "expand", "edit", "delete");
        } else {
            return new ArrayList<>();
        }
    }
    
}
