package dev.littlebigowl.serveressentials.events;

import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.Colors;
import dev.littlebigowl.serveressentials.utils.ServerWebHook;
import dev.littlebigowl.serveressentials.utils.TeamUtil;

import org.bukkit.Bukkit;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

public class OnPlayerDeath implements Listener {

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {

        if(event.getEntity() instanceof Player) {
            String deathMessage = event.getDeathMessage();
            Player player = event.getEntity();

            if(deathMessage == null) {
                deathMessage = player.getName() + " died";
            }

            ServerWebHook serverWebHook = new ServerWebHook(
                Config.get().getString("DiscordWebhookURL"),
                "Server",
                Config.get().getString("DiscordWebhookAvatarURL")
            );
            serverWebHook.sendEmbed(Colors.toInt(Colors.DEATH), "[" + TeamUtil.getTeamPrefix(Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200)) + "] " + deathMessage, "https://minotar.net/avatar/" + player.getName() + ".png");

            int x = (int) player.getLocation().getX();
            int y = (int) player.getLocation().getY();
            int z = (int) player.getLocation().getZ();

            Bukkit.getLogger().info("\u001b[38;5;206m@Server \u001b[38;5;248m» \u001b[37;1m\033[3m" + deathMessage + "\u001b[0m\u001b[38;5;46m [X: " + x + ", Y: " + y + ", Z: " + z + "]\u001b[0m");
        }
    }
}
