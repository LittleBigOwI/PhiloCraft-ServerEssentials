package dev.littlebigowl.serveressentials.events;

import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.Colors;
import dev.littlebigowl.serveressentials.utils.ServerWebHook;
import dev.littlebigowl.serveressentials.utils.TeamUtil;

import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String msg = ChatColor.translateAlternateColorCodes('&', "&8[&4-&8]&7 " + player.getName());

        ServerWebHook serverWebHook = new ServerWebHook(
            Config.get().getString("DiscordWebhookURL"),
            "Server",
            Config.get().getString("DiscordWebhookAvatarURL")
        );
        serverWebHook.sendEmbed(Colors.DANGER, TeamUtil.getTeamPrefix(Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200)) + " " + event.getPlayer().getName() + " left the game.", "https://minotar.net/avatar/" + player.getName() + ".png");

        event.setQuitMessage(msg);

    }
}
