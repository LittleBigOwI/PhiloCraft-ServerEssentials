package dev.littlebigowl.serveressentials.events;

import org.bukkit.entity.Enderman;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityChangeBlockEvent;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;

public class OnEntityChangeBlock implements Listener {
    
    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {

        Entity entity = event.getEntity();
        if(entity instanceof Enderman) {
            Area area = ServerEssentials.database.getAreaFromPosition(entity.getLocation());
            
            if(area != null && !(area.permissions.get("doMobGriefing"))) {
                event.setCancelled(true);
            }
        }
    }

}
