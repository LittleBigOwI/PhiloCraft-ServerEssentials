package dev.littlebigowl.serveressentials.discordbot;

import javax.security.auth.login.LoginException;

import dev.littlebigowl.serveressentials.discordbot.commands.*;
import dev.littlebigowl.serveressentials.discordbot.events.OnButtonClick;
import dev.littlebigowl.serveressentials.discordbot.events.OnGuildVoiceLeave;
import dev.littlebigowl.serveressentials.discordbot.events.OnMessageReceived;
import dev.littlebigowl.serveressentials.discordbot.events.OnReadyEvent;
import dev.littlebigowl.serveressentials.discordbot.events.OnRedditLinkSend;
import dev.littlebigowl.serveressentials.models.Config;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;

import java.util.Timer;
import java.util.TimerTask;

public final class App {

    public JDA bot;
    public App() throws LoginException {

        JDA bot = JDABuilder.createDefault(Config.get().getString("BotToken"))
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.streaming(Bukkit.getOnlinePlayers().size() + " players online!", "https://twitch.tv/littlebigowi"))
                .build();

        bot.addEventListener(new PlayCommand());
        bot.addEventListener(new PauseCommand());
        bot.addEventListener(new ResumeCommand());
        bot.addEventListener(new StopCommand());
        bot.addEventListener(new ShuffleCommand());
        bot.addEventListener(new QueueCommand());
        bot.addEventListener(new LoopCommand());
        bot.addEventListener(new NowplayingCommand());
        bot.addEventListener(new SkipCommand());
        bot.addEventListener(new RemoveCommnand());
        bot.addEventListener(new JoinCommand());
        bot.addEventListener(new LeaveCommand());

        bot.addEventListener(new OnlineCommand());

        bot.addEventListener(new OnReadyEvent(bot));
        bot.addEventListener(new OnGuildVoiceLeave());
        bot.addEventListener(new OnMessageReceived());
        bot.addEventListener(new OnRedditLinkSend());
        bot.addEventListener(new OnButtonClick());

        this.bot = bot;

        new Timer().schedule(new TimerTask(){
            public void run(){
                bot.getPresence().setActivity(Activity.streaming( Bukkit.getOnlinePlayers().size() + " players online!", "https://twitch.tv/littlebigowi"));
            }},0,45_000);
    }

    public void stopBot() {
        Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + this.bot.getSelfUser().getName() + "\u001b[0m\u001b[37;1m successfully logged out.\u001b[0m");
        this.bot.shutdown();
    }
}
