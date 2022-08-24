package dev.littlebigowl.serveressentials.discordbot.lavaplayer;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

public class TrackScheduler extends AudioEventAdapter {
    
    public final AudioPlayer audioPlayer;
    public final BlockingQueue<AudioTrack> queue;
    private boolean loop = false;
    private AudioTrack lastTrack;

    public TrackScheduler(AudioPlayer audioPlayer) {
        this.audioPlayer = audioPlayer;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void queue(AudioTrack track) {
        if(!this.audioPlayer.startTrack(track, true)) {
            this.queue.offer(track);
        }
    }

    public void nextTrack() {
        this.audioPlayer.startTrack(this.queue.poll(), false);
    }

    public void shuffle() {
        Collections.shuffle((List<?>) this.queue);
    }

    public void clearQueue() {
        this.queue.clear();
    }

    public BlockingQueue<AudioTrack> getQueue() {
        return this.queue;
    }

    public void loop(boolean repeat) {
        this.loop = repeat;
    }

    public boolean isLooping() {
        return loop;
    }

    public void skip() {
        nextTrack();
    }

    public AudioTrack remove(int index) {
        int i = 1;
        for(AudioTrack track : this.queue) {
            if(i==index) {
                this.queue.remove(track);
                return track;
            }
            i++;
        }
        return null;
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        
        this.lastTrack = track;
        if(endReason.mayStartNext)
        {
            if(loop)
                player.startTrack(lastTrack.makeClone(), false);
            else
                nextTrack();
        }
    }
}