package dev.littlebigowl.serveressentials.discordbot.events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.models.Config;

public class OnMessageReceived extends ListenerAdapter {

    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if(event.getChannel().getId().equals(Config.get().getString("DiscordChannelID"))) {

            if(event.getMember() == null) {
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                
                String message = ChatColor.translateAlternateColorCodes('&', "&9@" + Objects.requireNonNull(event.getMember()).getEffectiveName() + " &7»&f " + event.getMessage().getContentDisplay());
                player.sendMessage(message);
            }
            
            Bukkit.getLogger().info("[DI]" + Objects.requireNonNull(event.getMember()).getEffectiveName() + " » " + event.getMessage().getContentDisplay());
        }
    }
}
