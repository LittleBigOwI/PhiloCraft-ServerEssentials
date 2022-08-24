package dev.littlebigowl.serveressentials.discordbot.lavaplayer;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public class GuildMusicManager {

    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    public GuildMusicManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        
        this.audioPlayer.addListener(this.scheduler);
        
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }
    
    public AudioPlayerSendHandler getSendHandler() {
        return this.sendHandler;
    }

    public void pause() {
        if(!this.audioPlayer.isPaused()) { this.audioPlayer.setPaused(true); }
    }

    public void resume() {
        if(this.audioPlayer.isPaused()) { this.audioPlayer.setPaused(false); }
    }

    public void stop() {
        this.audioPlayer.stopTrack();
    }

    public boolean isPaused() {
        return this.audioPlayer.isPaused();
    }

    public boolean isPlaying() {
        if(this.audioPlayer.getPlayingTrack() != null) {
            return true;
        }
        return false;
    }

    public AudioTrack getTrack() {
        return audioPlayer.getPlayingTrack();
    }
}
