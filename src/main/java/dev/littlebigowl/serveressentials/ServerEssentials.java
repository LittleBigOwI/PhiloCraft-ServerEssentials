package dev.littlebigowl.serveressentials;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.littlebigowl.serveressentials.commands.*;
import dev.littlebigowl.serveressentials.discordbot.App;
import dev.littlebigowl.serveressentials.events.*;
import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.HomeUtil;
import dev.littlebigowl.serveressentials.utils.ParticleUtil;
import dev.littlebigowl.serveressentials.utils.SubmissionDetailsUtil;
import dev.littlebigowl.serveressentials.utils.TeamUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import javax.security.auth.login.LoginException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public final class ServerEssentials extends JavaPlugin {

    public static HashMap<Player, Player> tpa = new HashMap<>();
    private static ServerEssentials plugin;

    public static ServerEssentials getPlugin() {
        return plugin;
    }
    private App bot;

    @Override
    public void onEnable() {
        
        Config.setup();
        Config.get().options().copyDefaults(true);
        Config.save();

        plugin = this;
        try {
            HomeUtil.loadHomes();
            ParticleUtil.loadParticles();
            SubmissionDetailsUtil.loadSubmissions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            bot = new App();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(0x00ff00).setDescription("<:servericon:987016664799395900> **Server started.**").build());
        builder.setAvatarUrl("https://preview.redd.it/1wo65al6iox71.png?width=640&crop=smart&auto=webp&s=e9aab23333f9556cbeaa37587002dc9d7181137f");
        builder.setUsername("PhiloCraft");

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(Config.get().getString("DiscordWebhookURL"));
        WebhookClient client = webBuilder.build();
        client.send(message);

        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnAdvancementDoneEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);

        getCommand("tpa").setExecutor(new TpaCommand(this));
        getCommand("tpahere").setExecutor(new TpahereCommand(this));
        getCommand("tpaccept").setExecutor(new TpacceptCommand());
        getCommand("tpdeny").setExecutor(new TpdenyCommand());
        getCommand("tpcancel").setExecutor(new TpcancelCommand());
        getCommand("sethome").setExecutor(new SethomeCommand());
        getCommand("delhome").setExecutor(new DelhomeCommand());
        getCommand("homes").setExecutor(new HomesCommand());
        getCommand("home").setExecutor(new HomeCommand());
        getCommand("msg").setExecutor(new MsgCommand());
        getCommand("r").setExecutor(new RespondCommand());
        getCommand("rankinfo").setExecutor(new RankinfoCommand());
        getCommand("alteritem").setExecutor(new AlterItemCommand());
        getCommand("particle").setExecutor(new ParticleCommand());

        ((LoggerContext) LogManager.getContext(false)).getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME).addFilter(new LogFilter());

        Scoreboard scoreboard = Objects.requireNonNull(getServer().getScoreboardManager()).getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("playtime", "dummy", "playtime");
        Objective health = scoreboard.registerNewObjective("showHealth", Criterias.HEALTH,ChatColor.translateAlternateColorCodes('&', "&4❤"));

        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        TeamUtil teamUtil = new TeamUtil(scoreboard);

        Bukkit.getLogger().info("\u001b[38;5;206m@Server \u001b[38;5;248m» \u001b[37;1mServer started!\u001b[0m");

        getServer().getScheduler().runTaskTimer(this, () -> {

            for (Player player : getServer().getOnlinePlayers()) {
                Score score = objective.getScore(player.getName());
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                score.setScore(Math.round(playtime/60));

                Team team = teamUtil.getTeam(playtime);
                team.addEntry(player.getName());
                
                TeamUtil.updatePlayerTablist(player);
                player.setScoreboard(scoreboard);
            }
        }, 0, 10);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        bot.stopBot();

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(0xff0000).setDescription("<:servericon:987016664799395900> **Server stopped.**").build());
        builder.setAvatarUrl("https://preview.redd.it/1wo65al6iox71.png?width=640&crop=smart&auto=webp&s=e9aab23333f9556cbeaa37587002dc9d7181137f");
        builder.setUsername("PhiloCraft");

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(Config.get().getString("DiscordWebhookURL"));
        WebhookClient client = webBuilder.build();
        client.send(message);

        Bukkit.getLogger().info("\u001b[38;5;206m@Server \u001b[38;5;248m» \u001b[37;1mServer stopped.\u001b[0m");
    }
}
