package dev.littlebigowl.serveressentials.models;

import java.util.UUID;

import org.bukkit.Particle;

public class PlayerParticle {
    private UUID playerUUID;
    private Particle particle;
    
    public PlayerParticle(UUID playerUUID, Particle particle) {
        this.playerUUID = playerUUID;
        this.particle = particle;
    }

    public void setPlayerName(UUID playerUUID) {
        this.playerUUID = playerUUID;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public UUID getPlayerUUID() {
        return this.playerUUID;
    }

    public Particle getParticle() {
        return this.particle;
    }
}
