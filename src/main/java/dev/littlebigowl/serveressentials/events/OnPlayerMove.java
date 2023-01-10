package dev.littlebigowl.serveressentials.events;


import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;


import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;

public class OnPlayerMove implements Listener { 
    
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


        Area fromArea = ServerEssentials.database.getAreaFromPosition(event.getFrom());
        Area toArea = ServerEssentials.database.getAreaFromPosition(event.getTo());

        if(fromArea == null && toArea != null) {
            if(toArea.getEnterSplash() != null){ player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', "&b" + toArea.getEnterSplash()))); }
            
        } else if(toArea == null && fromArea != null){
            if(fromArea.getOutSplash() != null){ player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ChatColor.translateAlternateColorCodes('&', "&b" + fromArea.getOutSplash()))); }

        }

        
    }

}
