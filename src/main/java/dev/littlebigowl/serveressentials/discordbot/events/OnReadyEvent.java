package dev.littlebigowl.serveressentials.discordbot.events;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionType;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

public class OnReadyEvent extends ListenerAdapter{
    
    private final JDA bot;
    public OnReadyEvent(JDA bot) {
        this.bot = bot;
    }

    public void onReady(@Nonnull ReadyEvent event) {

        for(Guild guild : this.bot.getGuilds()) {
            guild.upsertCommand("play", "play audio in a voice channel")
                    .addOption(OptionType.STRING, "audio", "audio query", true)
                    .queue();

            guild.upsertCommand("pause", "pause audio in a voice channel")
                    .queue();
            
            guild.upsertCommand("resume", "resume audio in a voice channel")
                    .queue();
            
            guild.upsertCommand("stop", "stop audio in a voice channel")
                    .queue();

            guild.upsertCommand("shuffle", "shuffle your queue")
                    .queue();
            
            guild.upsertCommand("queue", "view your queue")
                    .addOption(OptionType.INTEGER, "page", "Queue page.", false)
                    .queue();
            
            guild.upsertCommand("loop", "loop audio")
                    .queue();

            guild.upsertCommand("nowplaying", "view current audio")
                    .queue();

            guild.upsertCommand("skip", "skip to next audio")
                    .queue();

            guild.upsertCommand("remove", "remove audio from your queue")
                    .addOption(OptionType.INTEGER, "index", "index of audio in your queue you want to remove", true)
                    .queue();

            guild.upsertCommand("join", "join voice channel")
                    .queue();

            guild.upsertCommand("leave", "leave voice channel")
                    .queue();

            guild.upsertCommand("online", "See who's online on the server")
                    .queue();
        }
        Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + event.getJDA().getSelfUser().getName() + "\u001b[0m\u001b[37;1m successfully logged in!\u001b[0m");
    }
}
