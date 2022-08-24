package dev.littlebigowl.serveressentials.discordbot.events;

import java.awt.Color;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import dev.littlebigowl.serveressentials.models.SubmissionDetails;
import dev.littlebigowl.serveressentials.utils.SubmissionDetailsUtil;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OnButtonClick extends ListenerAdapter{

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();

        for (SubmissionDetails submissionDetails : SubmissionDetailsUtil.submissions) {
            if(id.equals(submissionDetails.getId())) {

                long unix = Math.round(Float.parseFloat(submissionDetails.getCreationDate()));
                Date date = new Date(unix*1000L);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
                sdf.setTimeZone(TimeZone.getTimeZone("GMT+2"));

                String formatedDate = sdf.format(date);

                String desc = "Upvotes : " + submissionDetails.getUpvotes()
                    + "\nComments : " + submissionDetails.getCommentCount()
                    + "\nAwards : " + submissionDetails.getAwards()
                    + "\nCreated : " + formatedDate
                    + "\nSubreddit : " + submissionDetails.getSubredditName()
                    + "\nArchived : " + submissionDetails.isArchived()
                    + "\nNSFW : " + submissionDetails.isNSFW()
                    + "\nSpoiler" + submissionDetails.isSpoiler()
                    + "\nCrosspostable : " + submissionDetails.isCrosspostable();
                
                EmbedBuilder embed = new EmbedBuilder()
                    .setTitle(submissionDetails.getTitle(), submissionDetails.getPermalink())
                    .setDescription(desc)
                    .setColor(new Color(0xe082ff));

                event.replyEmbeds(embed.build()).setEphemeral(true).queue();

            }
        }
    }
    
}
