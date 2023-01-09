package dev.littlebigowl.serveressentials.events;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Area;

public class OnEntityDamage implements Listener {
    
    @EventHandler
    public void onEntityDamage(EntityDamageByEntityEvent event) {

        if(event.getEntity() instanceof Player && event.getDamager() instanceof Player) {
            Player attacker = (Player) event.getDamager();
            Player attacked = (Player) event.getEntity();
            
            Area attackerArea = ServerEssentials.database.getAreaFromPosition(attacker.getLocation());
            Area attackedArea = ServerEssentials.database.getAreaFromPosition(attacked.getLocation());

            if(attackerArea != null && !(attackerArea.permissions.get("doPVP"))) {
                event.setCancelled(true);
            } else if(attackedArea != null && !(attackedArea.permissions.get("doPVP"))) {
                event.setCancelled(true);
            }
        }

    }

}
