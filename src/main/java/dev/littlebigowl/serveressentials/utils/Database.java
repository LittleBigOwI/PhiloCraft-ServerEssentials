package dev.littlebigowl.serveressentials.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Arrays;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector2d;

import de.bluecolored.bluemap.api.math.Color;
import de.bluecolored.bluemap.api.math.Shape;
import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;
import dev.littlebigowl.serveressentials.models.Home;
import dev.littlebigowl.serveressentials.models.PlayerParticle;

public class Database {
    
    public HashMap<UUID, ArrayList<String>> cachedPlayerHomeNames = new HashMap<>();
    public HashMap<UUID, Particle> cachedPlayerParticles = new HashMap<>();
    public BidiMap<UUID, String> cachedPlayerDiscords = new DualHashBidiMap<>();
    public HashMap<UUID, String> cachedPlayerCodes = new HashMap<>();
    public HashMap<String, String> playerRoles = new HashMap<>();
    public HashMap<UUID, ArrayList<Area>> cachedplayerAreas = new HashMap<>();
    
    private Connection connection;
    private String host;
    private String database;
    private String user;
    private String password;

    public Database(String host, String database, String user, String password) throws SQLException, NumberFormatException, ParseException {
    
        this.host = host;
        this.database = database;
        this.user = user;
        this.password = password;

        connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Homes");
        ResultSet results = statement.executeQuery();

        ArrayList<String> allUUIDs = new ArrayList<>();
        ArrayList<Home> allPlayerHomes = new ArrayList<>();

        while(results.next()) {
            String playerUUID = results.getString("UUID");

            if (!allUUIDs.contains(playerUUID)) {
                allUUIDs.add(playerUUID);
            }

            String playerHomeName = results.getString("name");
            String playerHomeLocationString = results.getString("location");
            String[] playerHomeLocationValues = playerHomeLocationString.split(":");

            Location playerHomeLocation = new Location(
                Bukkit.getServer().getWorld("world"),
                Float.parseFloat(playerHomeLocationValues[0]),
                Float.parseFloat(playerHomeLocationValues[1]),
                Float.parseFloat(playerHomeLocationValues[2]),
                Float.parseFloat(playerHomeLocationValues[3]),
                Float.parseFloat(playerHomeLocationValues[4])
            );

            allPlayerHomes.add(new Home(UUID.fromString(playerUUID), playerHomeName, playerHomeLocation));
        }

        for(String playerUUID : allUUIDs) {
            cachedPlayerHomeNames.put(UUID.fromString(playerUUID), new ArrayList<>());
        }
        for(Home home : allPlayerHomes) {
            cachedPlayerHomeNames.get(home.getPlayerUUID()).add(home.getName());
        }

        statement = connection.prepareStatement("SELECT * FROM Particles");
        results = statement.executeQuery();

        while (results.next()) {
            cachedPlayerParticles.put(UUID.fromString(results.getString("UUID")), Particle.valueOf(results.getString("name")));
        }

        statement = connection.prepareStatement("SELECT * FROM Links");
        results = statement.executeQuery();

        while(results.next()) {
            cachedPlayerCodes.put(UUID.fromString(results.getString("UUID")), results.getString("code"));
        }

        statement = connection.prepareStatement("SELECT * FROM Accounts");
        results = statement.executeQuery();

        while(results.next()) {
            cachedPlayerDiscords.put(UUID.fromString(results.getString("UUID")), results.getString("ID"));
        }

        statement = connection.prepareStatement("SELECT * FROM Areas");
        results = statement.executeQuery();

        while(results.next()) {
            String[] areaStringLocation = results.getString("location").split(":");

            Location areaLocation = new Location(
                    Bukkit.getServer().getWorld("world"),
                    Float.parseFloat(areaStringLocation[0]),
                    Float.parseFloat(areaStringLocation[1]),
                    Float.parseFloat(areaStringLocation[2]),
                    Float.parseFloat(areaStringLocation[3]),
                    Float.parseFloat(areaStringLocation[4])
            );
            
            Area area = new Area(
                Integer.parseInt(results.getString("id")),
                UUID.fromString(results.getString("UUID")),
                results.getString("name"),
                results.getString("groupName"),
                Area.fromAreaStringChunks(results.getString("chunks")),
                new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(results.getString("creation")).getTime(),
                areaLocation,
                Boolean.parseBoolean(results.getString("doMobGriefing")),
                Boolean.parseBoolean(results.getString("doPVP")),
                new Color(results.getString("color")),
                results.getString("enterSplash"),
                results.getString("leaveSplash")
            );

            if(!cachedplayerAreas.containsKey(UUID.fromString(results.getString("UUID")))) {
                cachedplayerAreas.put(UUID.fromString(results.getString("UUID")), new ArrayList<>());
            }
            cachedplayerAreas.get(UUID.fromString(results.getString("UUID"))).add(area);
            area.draw();
        }

    }

    public void resetConnection() throws SQLException {
        try { connection.close(); } catch (Exception e) {}
        connection = DriverManager.getConnection("jdbc:mysql://" + host + "/" + database, user, password);
    }

    public void closeConnection() throws SQLException {
        connection.close();
    }

    //!Homes
    public Home createHome(UUID playerUUID, String name, Location location) throws SQLException {

        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double yaw = location.getYaw();
        double pitch = location.getPitch();

        String loc = x +
                ":" + y +
                ":" + z +
                ":" + yaw +
                ":" + pitch;

        connection.createStatement().executeUpdate("INSERT INTO Homes(UUID, name, location) VALUES ('" + playerUUID.toString() + "', '" + name + "', '" + loc + "')");
        
        Home home = new Home(playerUUID, name, location);
        try { 
            cachedPlayerHomeNames.get(playerUUID).add(home.getName()); 
        } catch (Exception e) {
            cachedPlayerHomeNames.put(playerUUID, new ArrayList<String>(Arrays.asList(home.getName())));
        }
        return home;
    }

    public ArrayList<Home> getHomes(UUID playerUUID) throws SQLException {

        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Homes WHERE UUID='" + playerUUID.toString() + "'");
        ResultSet results = statement.executeQuery();

        ArrayList<Home> playerHomes = new ArrayList<>();

        while(results.next()) {
            UUID playerHomeUUID = UUID.fromString(results.getString("UUID"));
            String playerHomeName = results.getString("name");
            
            String playerHomeLocationString = results.getString("location");
            String[] playerHomeLocationValues = playerHomeLocationString.split(":");

            Location playerHomeLocation = new Location(
                    Bukkit.getServer().getWorld("world"),
                    Float.parseFloat(playerHomeLocationValues[0]),
                    Float.parseFloat(playerHomeLocationValues[1]),
                    Float.parseFloat(playerHomeLocationValues[2]),
                    Float.parseFloat(playerHomeLocationValues[3]),
                    Float.parseFloat(playerHomeLocationValues[4])
            );

            playerHomes.add(new Home(playerHomeUUID, playerHomeName, playerHomeLocation));
        }

        return playerHomes;
    }

    public Home getHome(UUID playerUUID, String homeName) throws SQLException{
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Homes WHERE UUID='" + playerUUID.toString() + "' AND name='" + homeName + "'");
        ResultSet results = statement.executeQuery();


        while (results.next()) {
            UUID playerHomeUUID = UUID.fromString(results.getString("UUID"));
            String playerHomeName = results.getString("name");

            String playerHomeLocationString = results.getString("location");
            String[] playerHomeLocationValues = playerHomeLocationString.split(":");

            Location playerHomeLocation = new Location(
                    Bukkit.getServer().getWorld("world"),
                    Float.parseFloat(playerHomeLocationValues[0]),
                    Float.parseFloat(playerHomeLocationValues[1]),
                    Float.parseFloat(playerHomeLocationValues[2]),
                    Float.parseFloat(playerHomeLocationValues[3]),
                    Float.parseFloat(playerHomeLocationValues[4])
            );
            return new Home(playerHomeUUID, playerHomeName, playerHomeLocation);
        }
        return null;
    }

    public void deleteHome(UUID playerUUID, String homeName) throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM Homes WHERE UUID='" + playerUUID.toString() + "' AND name='" + homeName + "'");
        cachedPlayerHomeNames.get(playerUUID).remove(homeName);
    }

    //!Particles
    public PlayerParticle createParticle(UUID playerUUID, Particle particle) throws SQLException {
        
        PreparedStatement statement = connection.prepareStatement("SELECT * FROM Particles WHERE UUID='" + playerUUID.toString() + "'");
        ResultSet results = statement.executeQuery();

        if(!results.next()) {
            connection.createStatement().executeUpdate("INSERT INTO Particles(UUID, name) VALUES ('" + playerUUID.toString() + "', '" + particle.name() + "')");
            cachedPlayerParticles.put(playerUUID, particle);
        } else {
            connection.createStatement().executeUpdate("UPDATE Particles SET name='" + particle.name() + "' WHERE UUID='" + playerUUID.toString() + "'");
            cachedPlayerParticles.remove(playerUUID);
            cachedPlayerParticles.put(playerUUID, particle);
        }

        return new PlayerParticle(playerUUID, particle);
    }

    public PlayerParticle getParticle(UUID playerUUID) throws SQLException {
        
        PreparedStatement statement = connection.prepareStatement("SELECT name FROM Particles WHERE UUID='" + playerUUID.toString() + "'");
        statement.setMaxRows(1);
        ResultSet result = statement.executeQuery();

        Particle particle = Particle.valueOf(result.getString("name"));

        return new PlayerParticle(playerUUID, particle);
    }

    public void deleteParticle(UUID playerUUID) throws SQLException {
        connection.createStatement().executeUpdate("DELETE FROM Particles WHERE UUID='" + playerUUID.toString() + "'");
        cachedPlayerParticles.remove(playerUUID);
    }

    //!Links
    public void setupLink(UUID playerUUID, String code) throws SQLException {
        connection.createStatement().executeUpdate("INSERT INTO Links VALUES('" + playerUUID + "', '" + code + "')");
        cachedPlayerCodes.put(playerUUID, code);
    }

    public boolean completeLink(String id, String code) throws SQLException {
        
        if(!cachedPlayerCodes.containsValue(code)) {
            return false;
        }
        
        PreparedStatement statement = connection.prepareStatement("SELECT UUID FROM Links WHERE code='" + code + "'");
        statement.setMaxRows(1);
        ResultSet result = statement.executeQuery();
        
        

        if(result.isBeforeFirst()) {
            result.next();
            UUID playerUUID = UUID.fromString(result.getString("UUID"));

            connection.createStatement().executeUpdate("DELETE FROM Links WHERE code='" + code + "'");
            connection.createStatement().executeUpdate("INSERT INTO Accounts VALUES('" + playerUUID + "', '" + id + "')");

            cachedPlayerDiscords.put(playerUUID, id);
            return true;
        } else {
            return false;
        }
    }

    //!Areas
    public ArrayList<String> getAreaNames(UUID playerUUID) {
        ArrayList<String> areaNames = new ArrayList<>();
        ArrayList<Area> areas = ServerEssentials.database.cachedplayerAreas.get(playerUUID);
        
        if(areas != null) {
            for(Area area : areas) {
                areaNames.add(area.getName());
            }
        }
        return areaNames;
    }

    public ArrayList<Area> getAreas() {
        ArrayList<Area> allAreas = new ArrayList<>();

        for(ArrayList<Area> playerAreas : ServerEssentials.database.cachedplayerAreas.values()) {
            for(Area playerArea : playerAreas) {
                allAreas.add(playerArea);
            }
        }

        return allAreas;
    }

    public ArrayList<Shape> getAreaShapes() {
        ArrayList<Area> allAreas = this.getAreas();
        ArrayList<Shape> allShapes = new ArrayList<>();

        for(Area area : allAreas) {
            for(Shape shape : area.chunks) {
                allShapes.add(shape);
            }
        }

        return allShapes;
    }

    public Area getAreaFromPosition(Shape shape) {
        ArrayList<Area> allAreas = this.getAreas();

        int i = 0;
        boolean found = false;
        Area area = null;

        while(i < allAreas.size() && !(found)) {
            int j = 0;
            ArrayList<Shape> chunks = allAreas.get(i).chunks;
            
            while(j < chunks.size() && (shape.getPoint(0).getFloorX() != chunks.get(j).getPoint(0).getFloorX() || shape.getPoint(0).getFloorY() != chunks.get(j).getPoint(0).getFloorY())) {
                j++;
            }
            found = (j != chunks.size());
            i++;
        }

        if(found) {
            area = allAreas.get(i-1);
        }

        return area;
    }

    public Area getAreaFromPosition(Location loc) {
        int x = loc.getChunk().getX()*16;
        int z = loc.getChunk().getZ()*16;
        Shape shape =  new Shape(new Vector2d(x, z), new Vector2d(x, z+16), new Vector2d(x+16, z+16), new Vector2d(x+16, z));

        return this.getAreaFromPosition(shape);
    }

    public Area getAreaByName(UUID playerUUID, String name) {
        ArrayList<Area> areas = ServerEssentials.database.cachedplayerAreas.get(playerUUID);
        Area selectedArea = null;
        for(Area area : areas) {
            if(area.getName().equals(name)) {
                selectedArea = area;
            }
        }

        return selectedArea;
    }

    public Area createArea(String name, UUID playerUUID, Shape shape, Color color, Location location) throws SQLException {
        
        double x = location.getX();
        double y = location.getY();
        double z = location.getZ();
        double yaw = location.getYaw();
        double pitch = location.getPitch();

        String loc = x +
                ":" + y +
                ":" + z +
                ":" + yaw +
                ":" + pitch;

        Area area = new Area(name, playerUUID, shape, color, location);
        ServerEssentials.database.cachedplayerAreas.get(playerUUID).add(area);
        
        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());

        String enterSplash = area.getEnterSplash();
        String outSplash = area.getOutSplash();

        if(enterSplash != null) {
            enterSplash = "'" + enterSplash.replace("'", "''") + "'";
        }

        if(outSplash != null) {
            outSplash = "'" + outSplash.replace("'", "''") + "'";
        }

        connection.createStatement().executeUpdate(
            "INSERT INTO Areas VALUES(" + area.getId() + ", '" + 
            playerUUID.toString() + "', '" + 
            name.replace("'", "''") + "', '" +
            area.getGroupName().replace("'", "''") + "', '" +
            area.getAreaStringChunks() + "', " + 
            "FROM_UNIXTIME(" + area.creationDate + "), '" + 
            loc + "', " +
            area.permissions.get("doMobGriefing") + ", " +
            area.permissions.get("doPVP") + ", '" +
            hexColor + "', " +
            enterSplash + ", " +
            outSplash + ")"
        );
        
        return area;
    }

    public boolean expandArea(Area area, Shape shape) throws SQLException {
        boolean worked = area.addChunk(shape);

        if(worked) {
            connection.createStatement().executeUpdate("UPDATE Areas SET chunks='" + area.getAreaStringChunks() + "' WHERE ID=" + area.getId());
        }

        return worked;
    }

    public void updateAreaName(Area area, String name) throws SQLException {
        area.setName(name);
        connection.createStatement().executeUpdate("UPDATE Areas SET name='" + name.replace("'", "''") + "' WHERE ID=" + area.getId());
    }

    public void updateAreaGroupName(Area area, String groupName) throws SQLException {
        ArrayList<Area> playerAreas = ServerEssentials.database.cachedplayerAreas.get(area.getPlayer().getUniqueId());
        
        for(Area playerArea : playerAreas) {
            playerArea.setGroupName(groupName);
            connection.createStatement().executeUpdate("UPDATE Areas SET groupName='" + groupName.replace("'", "''") + "' WHERE ID=" + playerArea.getId());
        }
    }

    public void updateAreaColor(Area area, Color color) throws SQLException {
        area.setColor(color);
        String hexColor = String.format("#%02x%02x%02x", color.getRed(), color.getGreen(), color.getBlue());
        connection.createStatement().executeUpdate("UPDATE Areas SET color='" + hexColor + "' WHERE ID=" + area.getId());
    }

    public void updateAreaPermissions(Area area, String permission, boolean value) throws SQLException {
        area.permissions.put(permission, value);
        connection.createStatement().executeUpdate("UPDATE Areas SET " + permission + "=" + value + " WHERE ID=" + area.getId());
    }

    public void updateAreaEnterSplash(Area area, String splash) throws SQLException {
        area.setEnterSplash(splash);
        connection.createStatement().executeUpdate("UPDATE Areas SET enterSplash='" + splash.replace("'", "''") + "' WHERE ID=" + area.getId());
    }

    public void updateAreaOutSplash(Area area, String splash) throws SQLException {
        area.setOutSplash(splash);
        connection.createStatement().executeUpdate("UPDATE Areas SET leaveSplash='" + splash.replace("'", "''") + "' WHERE ID=" + area.getId());
    }

    public void removeAreaChunk(Area area, Shape shape) throws SQLException {
        area.removeChunk(shape);
        connection.createStatement().executeUpdate("UPDATE Areas SET chunks='" + area.getAreaStringChunks() + "' WHERE ID=" + area.getId());
    }

    public void deleteArea(Area area) throws SQLException {
        ServerEssentials.database.cachedplayerAreas.get(area.getPlayer().getUniqueId()).remove(area);
        connection.createStatement().executeUpdate("DELETE FROM Areas WHERE ID='" + area.getId() + "'");
        area.delete();
    }

    public int getPlayerAvailableChunks(Player player) {
        int claimedChunks = 0;
        int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200)/60;
        ArrayList<Area> playerAreas = ServerEssentials.database.cachedplayerAreas.get(player.getUniqueId());

        if(playerAreas == null) {
            return playtime;
        }
        
        for(Area playerArea : playerAreas) {
            claimedChunks += playerArea.chunks.size();
        }

        return playtime - claimedChunks;
    }

    public ArrayList<String> getAreaStringIds() {
        ArrayList<String> names = new ArrayList<>();
        for(Area area : this.getAreas()) {
            names.add(Integer.toString(area.getId()));
        }

        return names;
    }

    public Area getAreaById(int id) {
        for(Area area : this.getAreas()) {
            if(area.getId() == id) {
                return area;
            }
        }
        return null;
    }

}

