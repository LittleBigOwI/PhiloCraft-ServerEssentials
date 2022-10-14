package dev.littlebigowl.serveressentials.discordbot.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.VoiceChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

import dev.littlebigowl.serveressentials.events.LogFilter;

import java.awt.*;
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
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Join:937006558699724801> Joined voice channel.").setColor(new Color(0x5865f2)).build()).queue();

            LogFilter.logBotCommand(event, event.getName());
        }

    }

}

