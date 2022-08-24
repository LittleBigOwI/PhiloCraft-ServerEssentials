package dev.littlebigowl.serveressentials.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.littlebigowl.serveressentials.models.Config;

import org.bukkit.Bukkit;
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

            WebhookMessageBuilder builder = new WebhookMessageBuilder();
            builder.addEmbeds(new WebhookEmbedBuilder().setColor(0xaaaaaa).setAuthor(new WebhookEmbed.EmbedAuthor(deathMessage, "https://minotar.net/avatar/" + player.getName() + ".png", "")).build());
            builder.setAvatarUrl("https://preview.redd.it/1wo65al6iox71.png?width=640&crop=smart&auto=webp&s=e9aab23333f9556cbeaa37587002dc9d7181137f");
            builder.setUsername("PhiloCraft");

            WebhookMessage message = builder.build();

            WebhookClientBuilder webBuilder = new WebhookClientBuilder(Config.get().getString("DiscordWebhookURL"));
            WebhookClient client = webBuilder.build();
            client.send(message);

            int x = (int) player.getLocation().getX();
            int y = (int) player.getLocation().getY();
            int z = (int) player.getLocation().getZ();

            Bukkit.getLogger().info("\u001b[38;5;206m@Server \u001b[38;5;248m» \u001b[37;1m\033[3m" + deathMessage + "\u001b[0m\u001b[38;5;46m [X: " + x + ", Y: " + y + ", Z: " + z + "]\u001b[0m");
        }
    }
}
