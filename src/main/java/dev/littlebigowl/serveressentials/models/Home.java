package dev.littlebigowl.serveressentials.models;

import java.util.UUID;

import org.bukkit.Location;

public class Home {
    private UUID playerUUID;
    private String name;
    private Location location;
    
    public Home(UUID playerUUID, String name, Location playerHomeLocation) {
        this.playerUUID = playerUUID;
        this.name = name;
        this.location = playerHomeLocation;
    }

    public void setPlayerUUID(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public UUID getPlayerUUID() {
        return playerUUID;
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }
}
