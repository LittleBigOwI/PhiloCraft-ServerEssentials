package dev.littlebigowl.serveressentials.models;

import org.bukkit.Particle;

public class PlayerParticle {
    private String playerName;
    private Particle particle;
    public PlayerParticle(String playerName, Particle particle) {
        this.playerName = playerName;
        this.particle = particle;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public String getPlayerName() {
        return this.playerName;
    }

    public Particle getParticle() {
        return this.particle;
    }
}
