package dev.littlebigowl.serveressentials.models;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class Config {

    private static File file;
    private static FileConfiguration customFile;

    public static void setup() {
        file = new File(Bukkit.getServer().getPluginManager().getPlugin("ServerEssentials").getDataFolder(), "config.yml");

        if(!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // Pass
            }
        }

        customFile = YamlConfiguration.loadConfiguration(file);
        customFile.addDefault("DiscordWebhookURL", "None");
        customFile.addDefault("DiscordWebhookAvatarURL", "None");
        customFile.addDefault("DiscordChannelID", "None");
        customFile.addDefault("BotToken", "None");
        customFile.addDefault("DatabaseHost", "None");
        customFile.addDefault("DatabaseName", "None");
        customFile.addDefault("DatabaseUser", "None");
        customFile.addDefault("DatabasePassword", "None");
        customFile.addDefault("GuestRole", "None");
        customFile.addDefault("PlayerRole", "None");
        customFile.addDefault("PlayerPlusRole", "None");
        customFile.addDefault("MemberRole", "None");
        customFile.addDefault("PhiloCrafterRole", "None");
        customFile.addDefault("PhiloCrafterPlusRole", "None");
    }

    public static FileConfiguration get() {
        return customFile;
    }

    public static void save(){
        try {
            customFile.save(file);
        } catch (IOException e) {
            // Pass
        }
    }

    public static void reload() {
        customFile = YamlConfiguration.loadConfiguration(file);
    }
    
}
