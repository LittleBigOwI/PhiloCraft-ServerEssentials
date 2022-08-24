package dev.littlebigowl.serveressentials.utils;

import com.google.gson.Gson;
import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

public class HomeUtil {

    private static ArrayList<Home> homes = new ArrayList<>();

    public static Home createHome(Player player, String name, Location location){

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
        Home home = new Home(player.getName(), name, loc);
        homes.add(home);

        try {
            saveHomes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return home;
    }

    public static int getHomeCount(Player player) {
        int i = 0;
        for(Home home : homes) {
            if(home.getPlayerName().equals(player.getName())) {
                i++;
            }
        }
        return i;
    }

    public static ArrayList<String> getHomeNames(Player player) {

        ArrayList<String> homeNames = new ArrayList<>();
        for(Home home : homes) {
            if(home.getPlayerName().equals(player.getName())) {
                homeNames.add(home.getName());
            }
        }
        return homeNames;
    }

    public static Home getHome(Player player, String name) {
        for(Home home : homes) {
            if(home.getName().equals(name) && home.getPlayerName().equals(player.getName())) {
                return home;
            }
        }
        return null;
    }

    public static Location getHomeLocation(Home home) {
        String homeLocationString = home.getLocation();
        String[] locationValuesString = homeLocationString.split(":");

        Location homeLoc = new Location(
                Bukkit.getServer().getWorld("world"),
                Float.parseFloat(locationValuesString[0]),
                Float.parseFloat(locationValuesString[1]),
                Float.parseFloat(locationValuesString[2]),
                Float.parseFloat(locationValuesString[3]),
                Float.parseFloat(locationValuesString[4])
        );

        return homeLoc;
    }

    public static void deleteHome(Player player, String name) {
        for(Home home : homes) {
            if(home.getName().equals(name) && home.getPlayerName().equals(player.getName())) {
                homes.remove(home);
                break;
            }
        }
        try {
            saveHomes();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void saveHomes() throws IOException {

        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/homes.json");
        file.getParentFile().mkdir();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(homes, writer);
        writer.flush();
        writer.close();
    }

    public static void loadHomes() throws IOException {

        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/homes.json");
        if(file.exists()) {
            Reader reader = new FileReader(file);
            try { Home[] h = gson.fromJson(reader, Home[].class); homes = new ArrayList<>(Arrays.asList(h)); } catch(Exception e) { homes = new ArrayList<>();}
        }
    }
}