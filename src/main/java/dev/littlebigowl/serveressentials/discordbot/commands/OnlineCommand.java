package dev.littlebigowl.serveressentials.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import dev.littlebigowl.serveressentials.events.LogFilter;

import java.awt.*;
import java.util.ArrayList;

public class OnlineCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

        if(event.getName().equals("online")){
            ArrayList<String> players = new ArrayList<>();
            
            for(Player player : Bukkit.getOnlinePlayers()) {

                players.add(player.getName());

            }
            
            if(Bukkit.getOnlinePlayers().size() == 0) { 
                event.replyEmbeds(new EmbedBuilder().setDescription("There are no players online.").setColor(new Color(0x5865f2)).build()).queue(); 
                return; 
            }

            StringBuffer embedContent = new StringBuffer();
            for(String playerName : players) {
                embedContent.append("`" + playerName + "`, ");
            }
            embedContent.delete(embedContent.length()-2, embedContent.length());

            event.replyEmbeds(new EmbedBuilder().setDescription(embedContent).setColor(new Color(0x5865f2)).setTitle("Online Players").build()).queue();

            LogFilter.logBotCommand(event, event.getName());
        }
    }
}
