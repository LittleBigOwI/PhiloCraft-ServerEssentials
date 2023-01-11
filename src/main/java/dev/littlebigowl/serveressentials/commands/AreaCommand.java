package dev.littlebigowl.serveressentials.commands;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.bukkit.Statistic;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;
import dev.littlebigowl.serveressentials.utils.TeamUtil;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;


public class AreaCommand implements CommandExecutor, TabCompleter{

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        
        if(sender instanceof Player && label.equalsIgnoreCase("area")) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            
            if(args[0].equals("create") && args.length >= 2) {
                int remainingPlayerChunks = ServerEssentials.database.getPlayerAvailableChunks(player);
                if(remainingPlayerChunks <= 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have enough to claim chunks anymore"));
                    return true;
                }

                String name = "";
                for(int i = 1; i < args.length; i++) {
                    name = name + " " + args[i];
                }
                name = name.substring(1);
                
                ArrayList<Area> areas = ServerEssentials.database.cachedplayerAreas.get(playerUUID);
                if(areas == null) { ServerEssentials.database.cachedplayerAreas.put(playerUUID, new ArrayList<Area>()); }
                areas = ServerEssentials.database.cachedplayerAreas.get(playerUUID);

                if(areas.size() >= 2) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou have reached the maximum amount of areas a player can have."));
                    return true;
                }

                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);

                Shape shape = new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z));
                Area presentArea = ServerEssentials.database.getAreaFromPosition(shape);
                
                if(presentArea == null) {
                    Color color = new Color(TeamUtil.getTeamColor(playtime).getColor().getRed(), TeamUtil.getTeamColor(playtime).getColor().getGreen(), TeamUtil.getTeamColor(playtime).getColor().getBlue());
                    Area area;
                    try {
                        ServerEssentials.database.resetConnection();
                        area = ServerEssentials.database.createArea(name, playerUUID, shape, color, player.getLocation());
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError creating area"));
                        return true;
                    }
                    
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSuccessfully created area"));
                    area.draw();

                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou can't create an area here"));
                }


            } else if(args[0].equals("expand") && args.length >= 2) {
                int remainingPlayerChunks = ServerEssentials.database.getPlayerAvailableChunks(player);
                if(remainingPlayerChunks <= 0) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have enough to claim chunks anymore"));
                    return true;
                }

                int x = player.getLocation().getChunk().getX()*16;
                int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                Shape shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)); //square
                
                String areaName = "";
                for(int i = 1; i < args.length; i++) {
                    areaName = areaName + " " + args[i];
                }
                areaName = areaName.substring(1);

                Area area = ServerEssentials.database.getAreaByName(playerUUID, areaName);
                
                try {
                    ServerEssentials.database.resetConnection();
                    if(area != null && ServerEssentials.database.expandArea(area, shape)) {
                       player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aAdded chunk to your area"));
                       area.draw();

                    } else if(area != null){
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis chunk can't be added to your area."));

                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThis area cannot be found."));

                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cError expanding area"));
                    return true;
                }


            } else if(args[0].equals("edit") && args.length >= 3) {
                Area area = ServerEssentials.database.getAreaFromPosition(player.getLocation());
                
                if(area == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no area at your position"));

                } else if(!(area.getPlayer().getUniqueId().equals(playerUUID))) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not own this area"));

                } else if(args[1].equals("name")) {
                    String areaName = "";
                    for(int i = 2; i < args.length; i++) {
                        areaName = areaName + " " + args[i];
                    }
                    areaName = areaName.substring(1);
                    
                    try {
                        ServerEssentials.database.resetConnection();
                        ServerEssentials.database.updateAreaName(area, areaName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                        return true;
                    }
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChanged area name to \"" + areaName + "\""));

                } else if(args[1].equals("color")) {
                    try{
                        int r = java.awt.Color.decode(args[2]).getRed();
                        int g = java.awt.Color.decode(args[2]).getGreen();
                        int b = java.awt.Color.decode(args[2]).getBlue();
                        Color color = new Color(r, g, b);

                        ServerEssentials.database.resetConnection();
                        ServerEssentials.database.updateAreaColor(area, color);
                        player.sendMessage(ChatColor.GREEN + "Changed area color to " + ChatColor.RESET + "" + ChatColor.of(new java.awt.Color(r, g, b)) + args[2]);

                    } catch(Exception e) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cInvalid color"));
                    }

                } else if(args[1].equals("groupName")) {
                    String groupName = "";
                    for(int i = 2; i < args.length; i++) {
                        groupName = groupName + " " + args[i];
                    }
                    groupName = groupName.substring(1);
                    try {
                        ServerEssentials.database.resetConnection();
                        ServerEssentials.database.updateAreaGroupName(area, groupName);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                        return true;
                    }
                    
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChanged area group name to \"" + groupName + "\""));
                
                } else if(args[1].equals("permissions")) {
                    boolean value = true;
                    if(args[3].equals("false")) { value = false; }

                    if(args[2].equals("doMobGriefing")) {
                        try {
                            ServerEssentials.database.resetConnection();
                            ServerEssentials.database.updateAreaPermissions(area, "doMobGriefing", value);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                            return true;
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChanged permission \"doMobGriefing\" to " + value));

                    } else if(args[2].equals("doPVP")){
                        try {
                            ServerEssentials.database.resetConnection();
                            ServerEssentials.database.updateAreaPermissions(area, "doPVP", value);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                            return true;
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aChanged permission \"doPVP\" to " + value));

                    } else {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThat is not a valid permission"));

                    }
                } else if(args[1].equals("enterSplash")) {
                    if(args[2].equals("remove")) {
                        area.setEnterSplash(null);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved enter splash text"));

                    } else if(args[2].equals("set")){
                        String splash = "";
                        for(int i = 3; i < args.length; i++) {
                            splash = splash + " " + args[i];
                        }
                        splash = splash.substring(1);

                        try {
                            ServerEssentials.database.resetConnection();
                            ServerEssentials.database.updateAreaEnterSplash(area, splash);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                            return true;
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSet the enter splash text to \"" + splash + "\""));
                    }

                    
                }  else if(args[1].equals("leaveSplash")) {
                    if(args[2].equals("remove")) {
                        area.setOutSplash(null);
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved leave splash text"));

                    } else if(args[2].equals("set")){
                        String splash = "";
                        for(int i = 3; i < args.length; i++) {
                            splash = splash + " " + args[i];
                        }
                        splash = splash.substring(1);

                        try {
                            ServerEssentials.database.resetConnection();
                            ServerEssentials.database.updateAreaOutSplash(area, splash);
                        } catch (SQLException e) {
                            e.printStackTrace();
                            player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                            return true;
                        }
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aSet the leave splash text to \"" + splash + "\""));
                    }


                }


            } else if(args[0].equals("subdue") && args.length == 1) {
                Area area = ServerEssentials.database.getAreaFromPosition(player.getLocation());
                if(area!= null && area.getPlayer().getUniqueId().equals(playerUUID)){   
                    int x = player.getLocation().getChunk().getX()*16;
                    int z = player.getLocation().getChunk().getZ()*16; //Bottom right corner of chunk
                    Shape shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z)); //square

                    try {
                        ServerEssentials.database.resetConnection();
                        ServerEssentials.database.removeAreaChunk(area, shape);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                        return true;
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved current chunk from your area"));
                } else if(area == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no area at your position"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not own this area"));
                }
                
            } else if(args[0].equals("info") && args.length >= 1) {
                Area area = ServerEssentials.database.getAreaFromPosition(player.getLocation()); 

                if(area != null) {
                    String areaOwner = area.getPlayer().getName();
                    String creationDate = new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss").format(new Date(area.creationDate));
                    int areaSurface = (16*16)*area.chunks.size();

                    TextComponent title = new TextComponent(ChatColor.translateAlternateColorCodes('&', "&f&lArea Info:&r"));
                    TextComponent idInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[ID] - &r&3" + area.getId()));
                    TextComponent nameInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[Name] - &r&3\"" + area.getName() + "\""));
                    TextComponent ownerInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[Owner] - &r&3" + areaOwner));
                    TextComponent surfaceInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[Surface] - &r&3" + areaSurface + "m²"));
                    TextComponent creationInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[Creation] - &r&3" + creationDate));
                    TextComponent griefInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[mobGriefing] - &r&3" + area.permissions.get("doMobGriefing")));
                    TextComponent pvpInfo = new TextComponent(ChatColor.translateAlternateColorCodes('&', "\n&r&b[PVP] - &r&3" + area.permissions.get("doPVP")));

                    surfaceInfo.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text(ChatColor.translateAlternateColorCodes('&', "&b" + area.chunks.size() + "&r 16 by 16 chunks"))));

                    title.addExtra(idInfo);
                    title.addExtra(ownerInfo);
                    title.addExtra(nameInfo);
                    title.addExtra(surfaceInfo);
                    title.addExtra(creationInfo);
                    title.addExtra(griefInfo);
                    title.addExtra(pvpInfo);

                    player.spigot().sendMessage(title);
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no area at your position"));
                }


            } else if(args[0].equals("delete") && args.length == 1) {
                Area area = ServerEssentials.database.getAreaFromPosition(player.getLocation());

                if(area != null && area.getPlayer().getUniqueId().equals(playerUUID)){   
                    try {
                        ServerEssentials.database.resetConnection();
                        ServerEssentials.database.deleteArea(area);
                    } catch (SQLException e) {
                        e.printStackTrace();
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cSomething went wrong"));
                        return true;
                    }

                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&aRemoved area"));
                } else if(area == null) {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cThere is no area at your position"));
                } else {
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou do not own this area"));
                }

            } else if(args[0].equals("get") && args.length >= 2) {
                if(args[1].equals("chunks")) {
                    int remainingPlayerChunks = ServerEssentials.database.getPlayerAvailableChunks(player);
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&eYou have &6" + remainingPlayerChunks + "&e remaining chunks to claim"));

                } else if(args[1].equals("areas") && args.length == 2) {
                    ArrayList<Area> playerAreas = ServerEssentials.database.cachedplayerAreas.get(playerUUID);
                    StringBuffer areaList = new StringBuffer();

                    if(playerAreas == null || playerAreas.size() == 0) {
                        player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&cYou don't have any areas"));
                        return true;
                    }

                    areaList.append(ChatColor.translateAlternateColorCodes('&', "&eAreas&6[&e" + playerAreas.size() + "&6]&7: &6"));
                    int i = 0;
                    for(Area area : playerAreas) {
                        if(i%2 == 0) {
                            areaList.append(area.getName()).append(ChatColor.translateAlternateColorCodes('&', " &7| &e"));
                        } else {
                            areaList.append(area.getName()).append(ChatColor.translateAlternateColorCodes('&', " &7| &6"));
                        }
                        i++;
                    }
                    areaList.delete(areaList.length()-7, areaList.length());
                    player.sendMessage(ChatColor.translateAlternateColorCodes('&', areaList.toString()));
                
                }
            
            } else if(args[0].equals("teleport") && args.length >= 1) {
                
            }
        }
    
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        Player player = null;
        List<String> permissions = Arrays.asList("doMobGriefing", "doPVP");
        if(sender instanceof Player) {
            player = (Player)sender;
        }

        if(label.equalsIgnoreCase("area") && args.length == 1) {
            return Arrays.asList("create", "expand", "edit", "delete", "subdue", "info", "get"); //teleport

        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("expand")) {
            return ServerEssentials.database.getAreaNames(player.getUniqueId());

        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("edit")) {
            return Arrays.asList("name", "color", "groupName", "permissions", "enterSplash", "leaveSplash");

        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("get")) {
            return Arrays.asList("chunks", "areas");

        } else if(label.equalsIgnoreCase("area") && args.length == 2 && args[0].equals("teleport")) {
            return new ArrayList<>();//ServerEssentials.database.getAreaStringIds();

        } else if(label.equalsIgnoreCase("area") && args.length == 3 && args[0].equals("edit") && args[1].equals("permissions")) {
            return permissions;

        } else if(label.equalsIgnoreCase("area") && args.length == 3 && args[0].equals("edit") && (args[1].equals("enterSplash") || args[1].equals("leaveSplash"))) {
            return Arrays.asList("set", "remove");

        } else if(label.equalsIgnoreCase("area") && args.length == 4 && args[0].equals("edit") && args[1].equals("permissions") && permissions.contains(args[2])) {
            return Arrays.asList("true", "false");

        } else {
            return new ArrayList<>();
        }
    }
    
}
