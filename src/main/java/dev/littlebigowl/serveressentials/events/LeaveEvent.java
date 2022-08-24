package dev.littlebigowl.serveressentials.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.littlebigowl.serveressentials.models.Config;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveEvent implements Listener {

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        String msg = ChatColor.translateAlternateColorCodes('&', "&8[&4-&8]&7 " + player.getName());

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(0xff0000).setAuthor(new WebhookEmbed.EmbedAuthor(event.getPlayer().getName() + " left the game.", "https://minotar.net/avatar/" + player.getName() + ".png", "")).build());
        builder.setAvatarUrl("https://preview.redd.it/1wo65al6iox71.png?width=640&crop=smart&auto=webp&s=e9aab23333f9556cbeaa37587002dc9d7181137f");
        builder.setUsername("PhiloCraft");

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(Config.get().getString("DiscordWebhookURL"));
        WebhookClient client = webBuilder.build();
        client.send(message);

        event.setQuitMessage(msg);

    }
}
