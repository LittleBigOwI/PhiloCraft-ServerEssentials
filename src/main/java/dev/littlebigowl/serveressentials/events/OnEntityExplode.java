package dev.littlebigowl.serveressentials.events;

import org.bukkit.entity.Creeper;
import org.bukkit.entity.Entity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;

public class OnEntityExplode implements Listener{
    
    @EventHandler
    public void onEntityExplode(EntityExplodeEvent event) {

        Entity entity = event.getEntity();
        if(entity instanceof Creeper) {
            Area area = ServerEssentials.database.getAreaFromPosition(entity.getLocation());
            
            if(area != null && !(area.permissions.get("doMobGriefing"))) {
                event.setCancelled(true);
            }
        }

    }

}
