package dev.littlebigowl.serveressentials.events;


import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import dev.littlebigowl.serveressentials.utils.ParticleUtil;

public class OnPlayerMove implements Listener{
    
    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        ParticleUtil.updateParticles(event.getPlayer());
    }

}
