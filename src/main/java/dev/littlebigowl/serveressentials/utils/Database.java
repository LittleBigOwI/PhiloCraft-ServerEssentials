package dev.littlebigowl.serveressentials.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.Arrays;

import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;

import dev.littlebigowl.serveressentials.models.Home;
import dev.littlebigowl.serveressentials.models.PlayerParticle;
import dev.littlebigowl.serveressentials.models.Submission;

public class Database {
    
    public HashMap<UUID, ArrayList<String>> cachedPlayerHomeNames = new HashMap<>();
    public HashMap<UUID, Particle> cachedPlayerParticles = new HashMap<>();
    public HashMap<String, Submission> cachedPlayerSubmissions = new HashMap<>();
    
    private Connection connection;
    private String host;
    private String database;
    private String user;
    private String password;

    public Database(String host, String database, String user, String password) throws SQLException {
    
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

        statement = connection.prepareStatement("SELECT * FROM Submissions");
        results = statement.executeQuery();

        while (results.next()) {
            Submission submission = new Submission(
                results.getString("title"),
                results.getInt("upvotes"),
                results.getInt("comments"),
                results.getInt("awards"),
                Math.round(results.getDate("creationDate").getTime()),
                results.getString("url"),
                results.getString("permalink"),
                results.getString("subreddit"),
                results.getBoolean("archived"),
                results.getBoolean("nsfw"),
                results.getBoolean("spoiler"),
                results.getBoolean("crosspostable")
            );

            cachedPlayerSubmissions.put(results.getString("ID"), submission);
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

    //!Submissions
    public void createSubmission(Submission submission, String id) throws SQLException, IOException {

        connection.createStatement().executeUpdate(
            "INSERT INTO Submissions VALUES('"
            + id + "', '" 
            + submission.getTitle() + "', "
            + submission.getUpvotes() + ", "
            + submission.getComments() + ", "
            + submission.getAwards() + ", FROM_UNIXTIME("
            + submission.getCreationDateUNIX() + "), '"
            + submission.getPermalink() + "', '"
            + submission.getUrl() + "', '"
            + submission.getSubreddit() + "', "
            + submission.isArchived() + ", "
            + submission.isNSFW() + ", "
            + submission.isSpoiler() + ", "
            + submission.isCrosspostable()
            + ")"
        );

        cachedPlayerSubmissions.put(id, submission);

    }

}
