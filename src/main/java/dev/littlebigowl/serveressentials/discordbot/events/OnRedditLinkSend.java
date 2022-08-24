package dev.littlebigowl.serveressentials.discordbot.events;

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.models.Submission;
import dev.littlebigowl.serveressentials.models.Subreddit;
import dev.littlebigowl.serveressentials.utils.SubmissionDetailsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

public class OnRedditLinkSend extends ListenerAdapter{
    
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {

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

                if(redditSubmission.getUrl().contains("www.reddit.com")) {
                    
                    EmbedBuilder embed = new EmbedBuilder()
                        .setTitle(redditSubmission.getTitle(), redditSubmission.getUrl())
                        .setDescription(redditSubmission.getRawJsonData().get("selftext").getAsString())
                        .setColor(new Color(0x5865f2));

                    MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                        .setEmbeds(embed.build())
                        .setActionRow(
                            Button.primary(event.getMessageId(), "More Info"),
                            Button.link(redditSubmission.getUrl(), "Link")
                        );
                    message.queue();

                } else if(redditSubmission.getUrl().contains("i.redd.it")) {
                    
                    Subreddit redditSubmissionReddit;
                    try {
                        redditSubmissionReddit = redditSubmission.getSubreddit();
                    } catch (IOException e) {
                        e.printStackTrace();
                        return;
                    }

                    EmbedBuilder embed = new EmbedBuilder()
                        .setImage(redditSubmission.getUrl())
                        .setTitle(redditSubmission.getTitle(), redditSubmission.getUrl())
                        .setColor(new Color(0x5865f2))
                        .setFooter(redditSubmissionReddit.getName(), redditSubmissionReddit.getIconUrl());
                    
                    MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                        .setEmbeds(embed.build())
                        .setActionRow(
                            Button.primary(event.getMessageId(), "More Info"),
                            Button.link("https://www.reddit.com" + redditSubmission.getPermalink(), "Link")
                        );
                    
                    message.queue();

                } else if (redditSubmission.getUrl().contains("v.redd.it")) {
                    InputStream videoInputStream;
                    try {
                        videoInputStream = new URL(redditSubmission.getRawJsonData().get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("fallback_url").getAsString()).openStream();
                    } catch (Exception e) {
                        videoInputStream = null;
                    }

                    if(videoInputStream == null) {
                        System.out.println("Something went wrong 4.");
                        return;
                    }
                    
                    if(Integer.parseInt(redditSubmission.getRawJsonData().get("media").getAsJsonObject().get("reddit_video").getAsJsonObject().get("duration").getAsString()) <= 30) {
                        MessageAction message = event.getChannel().sendMessage("**From : " + event.getAuthor().getAsTag() + "**")
                            .addFile(videoInputStream, "redditVideo.mp4")
                            .setActionRow(
                                Button.primary(event.getMessageId(), "More Info"),
                                Button.link("https://www.reddit.com" + redditSubmission.getPermalink(), "Link")
                        );
                        
                        message.queue();
                    } else {
                        event.getChannel().sendMessage("** From : " + event.getAuthor().getAsTag() + "**\n*File is too large to upload*\n" + event.getMessage().getContentRaw()).queue();
                    }
                    
                }

                SubmissionDetailsUtil.createSubmissionDetails(redditSubmission, event.getMessageId());
                event.getMessage().delete().queue();
                return;
                
            }
        }

    }

}
