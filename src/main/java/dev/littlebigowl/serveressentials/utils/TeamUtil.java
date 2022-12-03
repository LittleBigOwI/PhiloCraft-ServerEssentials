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
        this.pcPlusTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.PHILOCRAFTER_PLUS + "PC+&f] "));
        this.pcTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.PHILOCRAFTER + "PC&f] "));
        this.memberTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.MEMBER + "M&f] "));
        this.playerPlusTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.PLAYER_PLUS + "P+&f] "));
        this.playerTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.PLAYER + "P&f] "));
        this.guestTeam.setPrefix(ChatColor.translateAlternateColorCodes('&', "&f[" + Colors.GUEST + "G&f] "));

        this.pcPlusTeam.setColor(ChatColor.GOLD);
        this.pcTeam.setColor(ChatColor.GOLD);
        this.memberTeam.setColor(ChatColor.AQUA);
        this.playerPlusTeam.setColor(ChatColor.DARK_AQUA);
        this.playerTeam.setColor(ChatColor.DARK_AQUA);
        this.guestTeam.setColor(ChatColor.GRAY);
    }

    public Team getTeam(int playtime) {
        if (playtime >= 300 && playtime < 1500) {
            return playerTeam;
        } else if (playtime >= 1500 && playtime < 5000) {
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

    public static String getTeamRole(int playtime) {
        if (playtime >= 300 && playtime < 1500) {
            return ServerEssentials.database.playerRoles.get("playerTeam");
        } else if (playtime >= 1500 && playtime < 5000) {
            return ServerEssentials.database.playerRoles.get("playerPlusTeam");
        } else if (playtime >= 5000 && playtime < 10000) {
            return ServerEssentials.database.playerRoles.get("memberTeam");
        } else if (playtime >= 10000 && playtime < 30000) {
            return ServerEssentials.database.playerRoles.get("pcTeam");
        } else if (playtime >= 30000) {
            return ServerEssentials.database.playerRoles.get("pcPlusTeam");
        } else {
            return ServerEssentials.database.playerRoles.get("guestTeam");
        }
    }

    public static String getTeamPrefix(int playtime) {
        if (playtime >= 300 && playtime < 1500) {
            return Characters.PLAYER_PREFIX;
        } else if (playtime >= 1500 && playtime < 5000) {
            return Characters.PLAYER_PLUS_PREFIX;
        } else if (playtime >= 5000 && playtime < 10000) {
            return Characters.MEMBER_PREFIX;
        } else if (playtime >= 10000 && playtime < 30000) {
            return Characters.PHILOCRAFTER_PREFIX;
        } else if (playtime >= 30000) {
            return Characters.PHILOCRAFTER_PLUS_PREFIX;
        } else {
            return Characters.GUEST_PREFIX;
        }
    }

    public static String getTeamColor(int playtime) {
        if (playtime >= 300 && playtime < 1500) {
            return Colors.PLAYER;
        } else if (playtime >= 1500 && playtime < 5000) {
            return Colors.PLAYER_PLUS;
        } else if (playtime >= 5000 && playtime < 10000) {
            return Colors.MEMBER;
        } else if (playtime >= 10000 && playtime < 30000) {
            return Colors.PHILOCRAFTER;
        } else if (playtime >= 30000) {
            return Colors.PHILOCRAFTER_PLUS;
        } else {
            return Colors.PHILOCRAFTER_PLUS;
        }
    }

    public static String getTerminalTeamColor(int playtime) {
        if (playtime >= 300 && playtime < 1500) {
            return Colors.PLAYER_UNICODE;
        } else if (playtime >= 1500 && playtime < 5000) {
            return Colors.PLAYER_PLUS_UNICODE;
        } else if (playtime >= 5000 && playtime < 10000) {
            return Colors.MEMBER_UNICODE;
        } else if (playtime >= 10000 && playtime < 30000) {
            return Colors.PHILOCRAFTER_UNICODE;
        } else if (playtime >= 30000) {
            return Colors.PHILOCRAFTER_PLUS_UNICODE;
        } else {
            return Colors.GUEST_UNICODE;
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

        player.setPlayerListHeader(ChatColor.translateAlternateColorCodes('&', "\n"+ Colors.SERVER_GRADIENT +"\n&7Welcome, &e"+player.getName()));
        player.setPlayerListFooter(ChatColor.translateAlternateColorCodes('&', "\n&7Ping: &a" + ping + " &7Online: &b" + players + " &7MSPT: &a" + mspt + " &7TPS: &a" + tps + "\n&9Discord &f» &7discord.gg/F2upgYUDFa" + "\n&aStore &f» &7https://littlebigowl.dev/pages/store.html"));
    }
}
