package dev.littlebigowl.serveressentials.events;

import java.util.Objects;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;


public class LogFilter extends AbstractFilter {

    @Override
    public Result filter(LogEvent event) {
        return event == null ? Result.NEUTRAL : isLoggable(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return isLoggable(msg.getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return isLoggable(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return msg == null ? Result.NEUTRAL : isLoggable(msg.toString());
    }

    private Result isLoggable(String msg) {

        String[] filters = {
            "UUID", 
            "com.mojang.authlib.GameProfile",
            "logged in with", 
            "issued server command:",
            "moved too quickly",
            "lost connection: Disconnected"
        };

        if (msg != null) {
            for(String filter : filters) {
                if(msg.contains(filter)) { return Result.DENY; }
            }
        }
        return Result.NEUTRAL;
    }

    public static void logCommand(CommandSender sender, String label, String[] args) {

        if(sender instanceof Player) {
            Player player = (Player) sender;

            StringBuilder commandArgs = new StringBuilder();
            for(String arg : args) {
                commandArgs.append(arg).append(" ");
            }
            Bukkit.getLogger().info("\u001b[38;5;206m@Server \u001b[38;5;248m» \u001b[37;1m\033[3m" + player.getName() + "\u001b[0m\u001b[37;1m issued server command /\033[3m" + label + " " + commandArgs.toString() + "\u001b[0m");
        }

    }

    public static void logBotCommand(SlashCommandInteractionEvent event, String label) {
        
        if(label == "play") {
            String audio = Objects.requireNonNull(event.getOption("audio")).getAsString();
            Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + Objects.requireNonNull(event.getMember()).getEffectiveName() + "\u001b[0m\u001b[37;1m issued bot command /\u001b[3m" + label + " " + audio + "\u001b[0m");
        
        } else if (label == "queue") {
            String page = Objects.requireNonNull(event.getOption("page")).getAsString();
            Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + Objects.requireNonNull(event.getMember()).getEffectiveName() + "\u001b[0m\u001b[37;1m issued bot command /\u001b[3m" + label + " " + page + "\u001b[0m");
        
        } else if (label == "remove") {
            String index = Objects.requireNonNull(event.getOption("index")).getAsString();
            Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + Objects.requireNonNull(event.getMember()).getEffectiveName() + "\u001b[0m\u001b[37;1m issued bot command /\u001b[3m" + label + " " + index + "\u001b[0m");
        
        } else {
            Bukkit.getLogger().info("\u001b[38;5;43m@Bot \u001b[38;5;248m» \u001b[37;1m\u001b[3m" + Objects.requireNonNull(event.getMember()).getEffectiveName() + "\u001b[0m\u001b[37;1m issued bot command /\u001b[3m" + label + "\u001b[0m");
        }

    }

}
