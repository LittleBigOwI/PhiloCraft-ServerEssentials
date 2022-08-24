package dev.littlebigowl.serveressentials.discordbot.lavaplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import java.awt.Color;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

public class PlayerManager {
    
    private static PlayerManager INSTANCE;
    private final Map<Long, GuildMusicManager> musicManagers;
    private final AudioPlayerManager audioPlayerManager;

    public PlayerManager(){
        this.musicManagers = new HashMap<>();
        this.audioPlayerManager = new DefaultAudioPlayerManager();
        
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public GuildMusicManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            final GuildMusicManager guildMusicManager = new GuildMusicManager(this.audioPlayerManager);
            guild.getAudioManager().setSendingHandler(guildMusicManager.getSendHandler());

            return guildMusicManager;
        });
    }

    public void loadAndPlay(SlashCommandInteractionEvent event, String trackURL) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        this.audioPlayerManager.loadItemOrdered(musicManager, trackURL, new AudioLoadResultHandler() {
           
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);

                String audioTitle = track.getInfo().title;
                if(audioTitle.length() > 24) {
                    audioTitle = audioTitle.substring(0, 24) + "...";
                }

                EmbedBuilder embed = new EmbedBuilder()
                    .setDescription("Queued [" + audioTitle + "](" + track.getInfo().uri + ") | " + event.getMember().getAsMention())
                    .setColor(new Color(0x5865f2));
                
                event.replyEmbeds(embed.build()).queue();
            }

            @Override
            public void playlistLoaded(AudioPlaylist audioPlaylist) {
                final List<AudioTrack> tracks = audioPlaylist.getTracks();
                if(!tracks.isEmpty()) {
                    musicManager.scheduler.queue(tracks.get(0));

                    String audioTitle = tracks.get(0).getInfo().title;
                    if(audioTitle.length() > 24) {
                        audioTitle = audioTitle.substring(0, 24) + "...";
                    }

                    EmbedBuilder embed = new EmbedBuilder()
                        .setDescription("Queued [" + audioTitle + "](" + tracks.get(0).getInfo().uri + ") | " + event.getMember().getAsMention())
                        .setColor(new Color(0x5865f2));
                
                    event.replyEmbeds(embed.build()).queue();
                } 
            }

            @Override
            public void noMatches() {
                event.reply("Couldn't retreive any matches for you query.").queue();
            }

            @Override
            public void loadFailed(FriendlyException e) {
                event.reply("Failed to load audio.").queue();
            }

        });
    }

    public void pause(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        if(!musicManager.isPaused()) {
            musicManager.pause();
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Pause:846709694612504588> Paused.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> Already paused.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void resume(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        if(musicManager.isPaused()) {
            musicManager.resume();
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Play:846709694948966400> Resumed.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> Already resumed.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void stop(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(musicManager.isPlaying()) {
            musicManager.scheduler.clearQueue();
            musicManager.stop();
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Stop:846712942221459478> Stopped.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> Nothing is playing.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void shuffle(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(musicManager.scheduler.queue.isEmpty()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> The queue is empty.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            musicManager.scheduler.shuffle();
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Shuffle:936980188623958046> Shuffled.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void showQueue(SlashCommandInteractionEvent event, int page) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(musicManager.scheduler.queue.isEmpty()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> The queue is empty.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            
            int pageItems = 10;
            int start = (page - 1) * pageItems;
            int end = start + pageItems;

            List<String> songs = new ArrayList<>();
            String trackTitle;

            for(AudioTrack track : musicManager.scheduler.getQueue()) {
                
                trackTitle = track.getInfo().title;
                if(trackTitle.length() > 28) {
                    trackTitle = trackTitle.substring(0, 28) + "...";
                }
                
                songs.add(trackTitle);
            }
            
            if(songs.size() <= end) {
                end = songs.size();
            }
            songs = songs.subList(start, end);
            
            int i = 0;
            String description = "";
            for(String songTitle : songs) {
                description = description + "`" + Integer.toString(1+start+i) + ".` " + songTitle + "\n";
                i++;
            }

            event.replyEmbeds(new EmbedBuilder().setDescription(description).setColor(new Color(0x5865f2)).build()).queue();
        }

    }

    public void loop(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        
        if(!musicManager.scheduler.isLooping()) {
            musicManager.scheduler.loop(true);
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Loop:936942637473226752> Enabled looping.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            musicManager.scheduler.loop(false);
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Loop:936942637473226752> Disabled looping.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void showCurrentTrack(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());
        final AudioTrack track = musicManager.getTrack();
        String trackTitle = track.getInfo().title;

        if(trackTitle.length() > 28) {
            trackTitle = trackTitle.substring(0, 28) + "...";
        }

        if(musicManager.isPlaying()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("Now playing [" + trackTitle + "](" + track.getInfo().uri + ") | " + event.getMember().getAsMention()).setColor(new Color(0x5865f2)).build()).queue();
        } else {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> Nothing is playing.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void skip(SlashCommandInteractionEvent event) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(musicManager.scheduler.queue.isEmpty()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> The queue is empty.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            musicManager.scheduler.skip();
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Skip:846709694973607956> Skipped.").setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public void remove(SlashCommandInteractionEvent event, int index) {
        final GuildMusicManager musicManager = this.getMusicManager(event.getGuild());

        if(musicManager.scheduler.queue.size() > index || musicManager.scheduler.queue.isEmpty()) {
            event.replyEmbeds(new EmbedBuilder().setDescription("<:Cross:969148307123359754> Index out of bounds.").setColor(new Color(0x5865f2)).build()).queue();
        } else {
            AudioTrack track = musicManager.scheduler.remove(index);
            String trackTitle = track.getInfo().title;

            if(trackTitle.length() > 28) {
                trackTitle = trackTitle.substring(0, 28) + "...";
            }

            event.replyEmbeds(new EmbedBuilder().setDescription("Removed [" + trackTitle + "](" + track.getInfo().uri + ") | " + event.getMember().getAsMention()).setColor(new Color(0x5865f2)).build()).queue();
        }
    }

    public static PlayerManager getInstance() {

        if(INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }

        return INSTANCE;
    }

}
