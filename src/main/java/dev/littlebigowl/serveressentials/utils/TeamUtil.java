package dev.littlebigowl.serveressentials.utils;

import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import dev.littlebigowl.serveressentials.ServerEssentials;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.RoundingMode;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.entity.Player;

public class TeamUtil {

    private Scoreboard scoreboard;
    private Team pcPlusTeam;
    private Team pcTeam;
    private Team memberTeam;
    private Team playerPlusTeam;
    private Team playerTeam;
    private Team guestTeam;

    public TeamUtil(Scoreboard scoreboard, String guestRole, String playerRole, String playerPlusRole, String memberRole, String pcRole, String pcPlusRole, String linkedRole) {
        this.scoreboard = scoreboard;
    
        this.pcPlusTeam = this.scoreboard.registerNewTeam("04Philocrafter+");
        this.pcTeam = this.scoreboard.registerNewTeam("05Philocrafter");
        this.memberTeam = this.scoreboard.registerNewTeam("06Member");
        this.playerPlusTeam = this.scoreboard.registerNewTeam("07Player+");
        this.playerTeam = this.scoreboard.registerNewTeam("08Player");
        this.guestTeam = this.scoreboard.registerNewTeam("09Guest");

        ServerEssentials.database.playerRoles.put("guestTeam", guestRole);
        ServerEssentials.database.playerRoles.put("playerTeam", playerRole);
        ServerEssentials.database.playerRoles.put("playerPlusTeam", playerPlusRole);
        ServerEssentials.database.playerRoles.put("memberTeam", memberRole);
        ServerEssentials.database.playerRoles.put("pcTeam", pcRole);
        ServerEssentials.database.playerRoles.put("pcPlusTeam", pcPlusRole);
        ServerEssentials.database.playerRoles.put("linked", linkedRole);

        setup();
    }

    private void setup() {
        this.pcPlusTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&6PC+&f] "));
        this.pcTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&6PC&f] "));
        this.memberTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&bM&f] "));
        this.playerPlusTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&3P+&f] "));
        this.playerTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&3P&f] "));
        this.guestTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[&7G&f] "));

        this.pcPlusTeam.setColor(ChatColor.GOLD);
        this.pcTeam.setColor(ChatColor.GOLD);
        this.memberTeam.setColor(ChatColor.AQUA);
        this.playerPlusTeam.setColor(ChatColor.DARK_AQUA);
        this.playerTeam.setColor(ChatColor.DARK_AQUA);
        this.guestTeam.setColor(ChatColor.GRAY);
    }

    public Team getTeam(int playtime) {
        if (playtime >= 300 && playtime < 1020) { //300 & 1500
            return playerTeam;
        } else if (playtime >= 1020 && playtime < 5000) { //1500 & 5000
            return playerPlusTeam;
        } else if (playtime >= 5000 && playtime < 10000) {
            return memberTeam;
        } else if (playtime >= 10000 && playtime < 30000) {
            return pcTeam;
        } else if (playtime >= 30000) {
            return pcPlusTeam;
        } else {
            return guestTeam;
        }
    }

    public static String getTeamName(int playtime) {
        if (playtime >= 300 && playtime < 1020) {
            return "playerTeam";
        } else if (playtime >= 1020 && playtime < 5000) {
            return "playerPlusTeam";
        } else if (playtime >= 5000 && playtime < 10000) {
            return "memberTeam";
        } else if (playtime >= 10000 && playtime < 30000) {
            return "pcTeam";
        } else if (playtime >= 30000) {
            return "pcPlusTeam";
        } else {
            return "guestTeam";
        }
    }

    public static String getTeamColor(int playtime) {
        if (playtime >= 300 && playtime < 1020) {
            return "&3";
        } else if (playtime >= 1020 && playtime < 5000) {
            return "&3";
        } else if (playtime >= 5000 && playtime < 10000) {
            return "&b";
        } else if (playtime >= 10000 && playtime < 30000) {
            return "&6";
        } else if (playtime >= 30000) {
            return "&6";
        } else {
            return "&7";
        }
    }

    public static String getTerminalTeamColor(int playtime) {
        if (playtime >= 300 && playtime < 1020) {
            return "\u001b[38;5;30m";
        } else if (playtime >= 1020 && playtime < 5000) {
            return "\u001b[38;5;30m";
        } else if (playtime >= 5000 && playtime < 10000) {
            return "\u001b[38;5;51m";
        } else if (playtime >= 10000 && playtime < 30000) {
            return "\u001b[38;5;172m";
        } else if (playtime >= 30000) {
            return "\u001b[38;5;172m";
        } else {
            return "\u001b[38;5;248m";
        }
    }

    private static double[] getTPS() {
        Object server = null;
        Field tps = null;
        try {
            if (server == null) {
                Server mc = Bukkit.getServer();

                Field consoleField = mc.getClass().getDeclaredField("console");
                consoleField.setAccessible(true);
                server = consoleField.get(mc);
            }
            if (tps == null) {
                tps = server.getClass().getSuperclass().getDeclaredField("recentTps");
                tps.setAccessible(true);
            }
            return (double[]) tps.get(server);
        } catch (IllegalAccessException | NoSuchFieldException ignored) {

        }
        return new double[]{20, 20, 20};
    }

    private static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    public static void updatePlayerTablist(Player player) {
        String ping;
        if(Math.round(player.getPing()) <= 1){
            ping = "1";
        } else {
            ping = Integer.toString(Math.round(player.getPing()));
        }
        String players = Integer.toString(Bukkit.getOnlinePlayers().size());
        String tps = Integer.toString(Math.round((float)getTPS()[0]));
        String mspt = Float.toString((float)round(getTPS()[0], 1));
        String serverGradientName = "&x&1&d&6&8&f&b&lP&x&2&b&7&2&f&b&lh&x&3&9&7&c&f&b&li&x&4&6&8&6&f&c&ll&x&5&4&9&0&f&c&lo&x&6&2&9&9&f&c&lC&x&7&0&a&3&f&c&lr&x&7&d&a&d&f&d&la&x&8&b&b&7&f&d&lf&x&9&9&c&1&f&d&lt";

        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', "\n"+ serverGradientName +"\n&7Welcome, &e"+player.getName()));
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', "\n&7Ping: &a" + ping + " &7Online: &b" + players + " &7MSPT: &a" + mspt + " &7TPS: &a" + tps + "\n&9Discord &f» &7discord.gg/MwvaMCaFTf" + "\n&aStore &f» &7store.littlebigowl.dev"));
    }
}
