package dev.littlebigowl.serveressentials.events;

import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.ServerWebHook;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;


public class OnChatEvent implements Listener {

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {

        Player player = event.getPlayer();
        int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);

        String prefix;
        String separator = "&7»&f";

        if (playtime >= 300 && playtime < 1500) {
            prefix = "&f[&3P&f]&3";
        } else if (playtime >= 1500 && playtime < 5000) {
            prefix = "&f[&3P+&f]&3";
        } else if (playtime >= 5000 && playtime < 10000) {
            prefix = "&f[&bM&f]&b";
        } else if (playtime >= 10000 && playtime < 30000) {
            prefix = "&f[&6PC&f]&6";
        } else if (playtime >= 30000) {
            prefix = "&f[&6PC+&f]&6";
        } else {
            prefix = "&f[&7G&f]&7";
        }

        String[] words = event.getMessage().split(" ");
        for(String word : words) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                if(p.getName().equals(word)){
                    Player mentionedPlayer = Bukkit.getPlayer(word);
                    assert mentionedPlayer != null;
                    mentionedPlayer.playSound(player.getLocation(), Sound.ENTITY_EXPERIENCE_ORB_PICKUP, 2, 1);
                    event.setMessage(event.getMessage().replaceAll(p.getName(), ChatColor.translateAlternateColorCodes('&', "&a&o" + p.getName() + "&r&f")));
                }
            }
        }

        ServerWebHook serverWebHook = new ServerWebHook(
            Config.get().getString("DiscordWebhookURL"),
            player.getName(),
            "https://minotar.net/avatar/" + player.getName() + ".png"
        );
        serverWebHook.sendMessage(event.getMessage());
        
        event.setFormat(ChatColor.translateAlternateColorCodes('&', prefix + " %s " + separator + " %s "));
    }
}
