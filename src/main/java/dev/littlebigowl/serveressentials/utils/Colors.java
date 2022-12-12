package dev.littlebigowl.serveressentials.utils;

import java.awt.Color;

import net.md_5.bungee.api.ChatColor;


public class Colors {

    public static final Color SUCCESS = Color.decode("#77dd77");
    public static final Color DANGER = Color.decode("#ff6961");
    public static final Color ADVANCEMENT = Color.decode("#9dcaeb");
    public static final Color DEATH = Color.decode("#bebebe");
    public static final Color DISCORD = Color.decode("#5865f2");

    public static final Color GUEST = Color.decode("#aaaaaa");
    public static final Color PLAYER = Color.decode("#00aaaa");
    public static final Color PLAYER_PLUS = Color.decode("#00aaaa");
    public static final Color MEMBER = Color.decode("#55ffff");
    public static final Color PHILOCRAFTER = Color.decode("#ffaa00");
    public static final Color PHILOCRAFTER_PLUS = Color.decode("#ffaa00");

    public static final String SERVER_GRADIENT = "&x&1&d&6&8&f&b&lP&x&2&b&7&2&f&b&lh&x&3&9&7&c&f&b&li&x&4&6&8&6&f&c&ll&x&5&4&9&0&f&c&lo&x&6&2&9&9&f&c&lC&x&7&0&a&3&f&c&lr&x&7&d&a&d&f&d&la&x&8&b&b&7&f&d&lf&x&9&9&c&1&f&d&lt";

    public static String toHexString(Color color) {
        return "#" + Integer.toHexString(Colors.PHILOCRAFTER_PLUS.getRGB()).substring(2);
    }

    public static int toInt(Color color) {
        return color.hashCode();
    }

    public static org.bukkit.ChatColor toBukkitChatColor(Color color) {
        return org.bukkit.ChatColor.valueOf(Colors.toBungeeChatColor(color).getName());
    }

    public static ChatColor toBungeeChatColor(Color color) {
        return ChatColor.of(Colors.toHexString(color));
    }
}
