package dev.littlebigowl.serveressentials;

import dev.littlebigowl.serveressentials.commands.*;
import dev.littlebigowl.serveressentials.discordbot.App;
import dev.littlebigowl.serveressentials.events.*;
import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.Colors;
import dev.littlebigowl.serveressentials.utils.Database;
import dev.littlebigowl.serveressentials.utils.Characters;
import dev.littlebigowl.serveressentials.utils.ServerWebHook;
import dev.littlebigowl.serveressentials.utils.TeamUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.*;

import de.bluecolored.bluemap.api.BlueMapAPI;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Objects;

public final class ServerEssentials extends JavaPlugin {

    public static Database database;
    public static BlueMapAPI blueMapAPI;
    public static HashMap<Player, Player> tpa = new HashMap<>();

    private static ServerEssentials plugin;
    private App bot;

    public static ServerEssentials getPlugin() {
        return plugin;
    }

    @Override
    public void onEnable() {
        
        Config.setup();
        Config.get().options().copyDefaults(true);
        Config.save();

        plugin = this;
        try {
            database = new Database(
                Config.get().getString("DatabaseHost"), 
                Config.get().getString("DatabaseName"), 
                Config.get().getString("DatabaseUser"), 
                Config.get().getString("DatabasePassword")
            );
            bot = new App();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        ServerWebHook serverWebHook = new ServerWebHook(
            Config.get().getString("DiscordWebhookURL"),
            "Server",
            Config.get().getString("DiscordWebhookAvatarURL")
        );
        serverWebHook.sendEmbed(Colors.toInt(Colors.SUCCESS), Characters.SERVER_START + " **Server started.**");

        getServer().getPluginManager().registerEvents(new JoinEvent(), this);
        getServer().getPluginManager().registerEvents(new LeaveEvent(), this);
        getServer().getPluginManager().registerEvents(new OnChatEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeath(), this);
        getServer().getPluginManager().registerEvents(new OnAdvancementDoneEvent(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);

        getCommand("tpa").setExecutor(new TpaCommand());
        getCommand("tpahere").setExecutor(new TpahereCommand());
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
        getCommand("link").setExecutor(new LinkCommand());
        getCommand("area").setExecutor(new AreaCommand());

        ((LoggerContext) LogManager.getContext(false)).getConfiguration().getLoggerConfig(LogManager.ROOT_LOGGER_NAME).addFilter(new LogFilter());

        Scoreboard scoreboard = Objects.requireNonNull(getServer().getScoreboardManager()).getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("playtime", "dummy", "playtime");
        Objective health = scoreboard.registerNewObjective("showHealth", Criterias.HEALTH,ChatColor.translateAlternateColorCodes('&', "&4❤"));

        objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        health.setDisplaySlot(DisplaySlot.BELOW_NAME);

        TeamUtil teamUtil = new TeamUtil(
            scoreboard,
            Config.get().getString("GuestRole"),
            Config.get().getString("PlayerRole"),
            Config.get().getString("PlayerPlusRole"),
            Config.get().getString("MemberRole"),
            Config.get().getString("PhiloCrafterRole"),
            Config.get().getString("PhiloCrafterPlusRole"),
            Config.get().getString("LinkedRole")
        );

        BlueMapAPI.onEnable(api -> {
           blueMapAPI = api;
        });

        Bukkit.getLogger().info(Characters.PLUGIN_PREFIX + "Plugin started.");

        getServer().getScheduler().runTaskTimer(this, () -> {

            for (Player player : getServer().getOnlinePlayers()) {
                Score score = objective.getScore(player.getName());
                int playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                score.setScore(Math.round(playtime/60));
                
                Team team = teamUtil.getTeam(playtime);

                if(!(team.hasEntry(player.getName()))) {
                    team.addEntry(player.getName());
                    
                    if(ServerEssentials.database.cachedPlayerDiscords.containsKey(player.getUniqueId())) {
                        bot.updateRoles(player, playtime, Config.get().getString("GuildID"));
                    }
                }
                TeamUtil.updatePlayerTablist(player);
                player.setScoreboard(scoreboard);
            }
        }, 0, 10);
    }

    @Override
    public void onDisable(){

        bot.stopBot();
        try { database.closeConnection(); } catch (SQLException e) {Bukkit.getLogger().info(e.toString());}

        ServerWebHook serverWebHook = new ServerWebHook(
            Config.get().getString("DiscordWebhookURL"),
            "Server",
            Config.get().getString("DiscordWebhookAvatarURL")
        );
        serverWebHook.sendEmbed(Colors.toInt(Colors.DANGER), Characters.SERVER_STOP + " **Server stopped.**");

        Bukkit.getLogger().info(Characters.PLUGIN_PREFIX + "Plugin stoppped.");
    }
}
