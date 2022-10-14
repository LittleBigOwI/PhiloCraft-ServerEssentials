package dev.littlebigowl.serveressentials.discordbot.events;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.models.Submission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class OnRedditLinkSend extends ListenerAdapter{
    
    @Override
    public void onMessageReceived(@Nonnull MessageReceivedEvent event) {

        if(event.getChannelType().isGuild() && event.getChannel().getId().equals(Config.get().getString("DiscordRedditChannelID"))) {
            if(event.getMessage().getContentRaw().startsWith("https://www.reddit.com/r/") && event.getMessage().getContentRaw().contains("?")) {
                String redditLink = event.getMessage().getContentRaw().split("\\?")[0] + ".json";
                Submission redditSubmission;
                try {
                    redditSubmission = Submission.fromUrl(redditLink);
                } catch (IOException e) {
                    e.printStackTrace();
                    return;
                }

                if(redditSubmission.getPermalink().contains("www.reddit.com")) {
                    
                    EmbedBuilder embed;
                    try {
                        embed = new EmbedBuilder()
                            .setTitle(redditSubmission.getTitle(), redditSubmission.getPermalink())
                            .setDescription(redditSubmission.getDescription(redditLink))
                            .setColor(new Color(0x5865f2));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                        .setEmbeds(embed.build())
                        .setActionRow(
                            Button.primary(event.getMessageId(), "More Info"),
                            Button.link(Objects.requireNonNull(redditSubmission.getUrl()), "Link")
                        );
                    message.queue();

                } else if(redditSubmission.getPermalink().contains("i.redd.it")) {

                    EmbedBuilder embed;
                    try {
                        embed = new EmbedBuilder()
                            .setImage(redditSubmission.getPermalink())
                            .setTitle(redditSubmission.getTitle(), redditSubmission.getPermalink())
                            .setColor(new Color(0x5865f2))
                            .setFooter(redditSubmission.getSubreddit(), redditSubmission.getSubredditIcon(redditLink));
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }
                    
                    MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                        .setEmbeds(embed.build())
                        .setActionRow(
                            Button.primary(event.getMessageId(), "More Info"),
                            Button.link(Objects.requireNonNull(redditSubmission.getUrl()), "Link")
                        );
                    
                    message.queue();

                } else if (redditSubmission.getPermalink().contains("v.redd.it")) {
                    InputStream videoInputStream;
                    try {
                        videoInputStream = redditSubmission.getVideo(redditLink);
                    } catch (Exception e) {
                        videoInputStream = null;
                    }

                    if(videoInputStream == null) {
                        System.out.println("Something went wrong 4.");
                        return;
                    }
                    
                    try {
                        if(redditSubmission.getVideoDuration(redditLink) <= 30) {
                            MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                                .addFile(videoInputStream, "redditVideo.mp4")
                                .setActionRow(
                                    Button.primary(event.getMessageId(), "More Info"),
                                    Button.link(Objects.requireNonNull(redditSubmission.getUrl()), "Link")
                            );
                            
                            message.queue();
                        } else {
                            event.getChannel().sendMessage("** From : " + event.getAuthor().getAsTag() + "**\n*File is too large to upload*\n" + event.getMessage().getContentRaw()).queue();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    
                }

                try {
                    ServerEssentials.database.resetConnection();
                    ServerEssentials.database.createSubmission(redditSubmission, event.getMessageId());
                } catch (Exception e) {
                    Bukkit.getLogger().info(e.toString());
                } 
                event.getMessage().delete().queue();
                return;
                
            }
        }

    }

}
