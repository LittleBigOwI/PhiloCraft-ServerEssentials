package dev.littlebigowl.serveressentials.discordbot;

import javax.security.auth.login.LoginException;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.discordbot.commands.*;
import dev.littlebigowl.serveressentials.discordbot.events.OnGuildVoiceLeave;
import dev.littlebigowl.serveressentials.discordbot.events.OnMessageReceived;
import dev.littlebigowl.serveressentials.discordbot.events.OnModalSubmit;
import dev.littlebigowl.serveressentials.discordbot.events.OnReadyEvent;
import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.Characters;
import dev.littlebigowl.serveressentials.utils.TeamUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public final class App {

    public JDA bot;
    public App() throws LoginException {

        JDA bot = JDABuilder.create(Config.get().getString("BotToken"), GatewayIntent.GUILD_MEMBERS, GatewayIntent.GUILD_VOICE_STATES, GatewayIntent.GUILD_PRESENCES, GatewayIntent.GUILD_EMOJIS, GatewayIntent.GUILD_MESSAGES, GatewayIntent.GUILD_MESSAGE_REACTIONS)
                .enableCache(CacheFlag.VOICE_STATE)
                .setActivity(Activity.playing(Bukkit.getOnlinePlayers().size() + " players online!"))
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

        bot.addEventListener(new LinkCommand());
        bot.addEventListener(new OnlineCommand());
        bot.addEventListener(new WhitelistCommand());

        bot.addEventListener(new OnReadyEvent(bot));
        bot.addEventListener(new OnGuildVoiceLeave());
        bot.addEventListener(new OnMessageReceived());
        bot.addEventListener(new OnModalSubmit(bot));

        this.bot = bot;

        new Timer().schedule(new TimerTask(){
            public void run(){
                int playerCount = Bukkit.getOnlinePlayers().size();
                if(playerCount == 1) {
                    bot.getPresence().setActivity(Activity.playing(playerCount + " player online!"));
                } else {
                    bot.getPresence().setActivity(Activity.playing(playerCount + " players online!"));
                }
            }},0,45_000);
    }

    public void updateRoles(Player player, int playtime, String guildId) {

        Guild guild = Objects.requireNonNull(bot.getGuildById(Objects.requireNonNull(guildId))) ;
        Member member = guild.getMemberById(Objects.requireNonNull(ServerEssentials.database.cachedPlayerDiscords.get(player.getUniqueId())));
        
        String roleId = Objects.requireNonNull(TeamUtil.getTeamRole(playtime));

        guild.addRoleToMember(Objects.requireNonNull(member), Objects.requireNonNull(guild.getRoleById(roleId))).queue();
    }

    public void stopBot() {
        Bukkit.getLogger().info(Characters.PLUGIN_PREFIX + "PhiloCord stopped.");
        this.bot.shutdown();
    }

}
