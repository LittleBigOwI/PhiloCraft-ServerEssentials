package dev.littlebigowl.serveressentials.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import dev.littlebigowl.serveressentials.events.LogFilter;
import dev.littlebigowl.serveressentials.utils.Colors;
import dev.littlebigowl.serveressentials.utils.Characters;

import java.util.Objects;

import javax.annotation.Nonnull;

public class JoinCommand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("join")) {

            if(!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) { event.reply("You need to be in a voice channel for this command to work.").queue(); return;}

            final AudioManager audioManager = Objects.requireNonNull(event.getGuild()).getAudioManager();
            final VoiceChannel memberChannel = (VoiceChannel) Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).getChannel();

            audioManager.openAudioConnection(memberChannel);
            event.replyEmbeds(new EmbedBuilder().setDescription(Characters.MUSIC_JOIN + " Joined voice channel.").setColor(Colors.DISCORD).build()).queue();

            LogFilter.logBotCommand(event, event.getName());
        }

    }

}

