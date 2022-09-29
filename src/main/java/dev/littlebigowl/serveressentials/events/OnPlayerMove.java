package dev.littlebigowl.serveressentials.events;


import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.littlebigowl.serveressentials.ServerEssentials;

public class OnPlayerMove implements Listener{
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        
        Player player = event.getPlayer();
        Particle particle;
        try {
            particle = ServerEssentials.database.cachedPlayerParticles.get(player.getUniqueId());
        } catch (Exception e) {
            particle = null;
        }
        if (particle != null) { player.getWorld().spawnParticle(particle, player.getLocation(), 1, 0, 0, 0, 0.1); }

    }

}
