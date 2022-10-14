package dev.littlebigowl.serveressentials.discordbot.commands;

import dev.littlebigowl.serveressentials.discordbot.lavaplayer.PlayerManager;
import dev.littlebigowl.serveressentials.events.LogFilter;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.Objects;

import javax.annotation.Nonnull;

public class RemoveCommnand extends ListenerAdapter {

    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("remove")) {

            if(!Objects.requireNonNull(Objects.requireNonNull(event.getMember()).getVoiceState()).inAudioChannel()) { event.reply("You need to be in a voice channel for this command to work.").queue(); return;}
            if(!Objects.requireNonNull(Objects.requireNonNull(Objects.requireNonNull(event.getGuild()).getSelfMember()).getVoiceState()).inAudioChannel()) { event.reply("I am not in a voice channel.").queue(); }

            int index = Objects.requireNonNull(event.getOption("index")).getAsInt();

            PlayerManager.getInstance().remove(event, index);

            LogFilter.logBotCommand(event, event.getName());
        }

    }

}

