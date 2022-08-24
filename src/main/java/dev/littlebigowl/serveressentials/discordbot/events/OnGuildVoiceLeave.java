package dev.littlebigowl.serveressentials.discordbot.events;

import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.managers.AudioManager;

public class OnGuildVoiceLeave extends ListenerAdapter {
 
    @Override
    public void onGuildVoiceLeave(GuildVoiceLeaveEvent event) {
        if(event.getChannelLeft().getMembers().size() == 1) {
            if(event.getChannelLeft().getMembers().get(0).getId().equals(event.getGuild().getSelfMember().getId())) {
                
                final AudioManager audioManager = event.getGuild().getAudioManager();
                audioManager.closeAudioConnection();

            }
        }
    }

}