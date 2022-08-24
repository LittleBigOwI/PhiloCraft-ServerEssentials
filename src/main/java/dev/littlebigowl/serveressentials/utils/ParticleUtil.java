package dev.littlebigowl.serveressentials.utils;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;

import org.bukkit.Particle;
import org.bukkit.entity.Player;

import com.google.gson.Gson;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.PlayerParticle;

public class ParticleUtil {
    
    private static ArrayList<PlayerParticle> playerParticles = new ArrayList<>();

    public static PlayerParticle setParticle(Player player, Particle particle) {
        
        for(PlayerParticle playerParticle : playerParticles) {
            if(playerParticle.getPlayerName().equals(player.getName())) {
                playerParticles.remove(playerParticle);
                break;
            }
        }

        PlayerParticle playerParticle = new PlayerParticle(player.getName(), particle);
        playerParticles.add(playerParticle);

        try {
            saveParticles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        
        return playerParticle;
    }

    public static PlayerParticle deleteParticle(Player player) {
        PlayerParticle particle = null;

        for(PlayerParticle playerParticle : playerParticles) {
            if(playerParticle.getPlayerName().equals(player.getName())) {
                particle = playerParticle;
                break;
            }
        }
        if(particle != null) { playerParticles.remove(particle); }
        try {
            saveParticles();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return particle;
    }

    public static PlayerParticle getParticle(Player player) {
        for(PlayerParticle playerParticle : playerParticles) {
            if(player.getName().equals(playerParticle.getPlayerName())) {
                return playerParticle;
            }
        }
        return null;
    }

    public static void saveParticles() throws IOException {
        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/particles.json");
        file.getParentFile().mkdir();
        file.createNewFile();

        Writer writer = new FileWriter(file, false);
        gson.toJson(playerParticles, writer);
        writer.flush();
        writer.close();
    }

    public static void loadParticles() throws IOException {

        Gson gson = new Gson();
        File file = new File(ServerEssentials.getPlugin().getDataFolder().getAbsolutePath() + "/particles.json");
        if(file.exists()) {
            Reader reader = new FileReader(file);
            try { PlayerParticle[] h = gson.fromJson(reader, PlayerParticle[].class); playerParticles = new ArrayList<>(Arrays.asList(h)); } catch(Exception e) { playerParticles = new ArrayList<>();}
        }
    }

    public static void updateParticles(Player player) {

        PlayerParticle particle = null;
        for(PlayerParticle playerParticle : playerParticles) {
            if(playerParticle.getPlayerName().equals(player.getName())) {
                particle = playerParticle;
            }
        }

        if(particle == null) { return; }

        
        player.getWorld().spawnParticle(particle.getParticle(), player.getLocation(), 1, 0, 0, 0, 0.1);
    }

}
