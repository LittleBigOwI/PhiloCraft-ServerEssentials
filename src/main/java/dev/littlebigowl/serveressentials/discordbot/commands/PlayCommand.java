package dev.littlebigowl.serveressentials.discordbot.commands;

import dev.littlebigowl.serveressentials.discordbot.lavaplayer.PlayerManager;
import dev.littlebigowl.serveressentials.events.LogFilter;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import java.net.URI;
import java.net.URISyntaxException;

import javax.annotation.Nonnull;

public class PlayCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("play")) {
            if(!event.getMember().getVoiceState().inAudioChannel()) { event.reply("You need to be in a voice channel for this command to work.").queue(); return;}

            if(!event.getGuild().getSelfMember().getVoiceState().inAudioChannel()) {
                final AudioManager audioManager = event.getGuild().getAudioManager();
                final VoiceChannel memberChannel = (VoiceChannel) event.getMember().getVoiceState().getChannel();

                audioManager.openAudioConnection(memberChannel);
            }

            String link = event.getOption("audio").getAsString();

            if(!isUrl(link)){
                link = "ytsearch:" + link + " audio";
            }

            PlayerManager.getInstance().loadAndPlay(event, link);

            LogFilter.logBotCommand(event, event.getName());
        }
    }
    public boolean isUrl(String url) {
        try {
            new URI(url);
            return true;
        } catch (URISyntaxException e) {
            return false;
        }
    }

}
