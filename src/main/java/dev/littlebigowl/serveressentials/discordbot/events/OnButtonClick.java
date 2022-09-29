package dev.littlebigowl.serveressentials.discordbot.events;

import java.awt.Color;
import java.io.IOException;

import org.bukkit.Bukkit;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.models.Submission;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class OnButtonClick extends ListenerAdapter{

    @Override
    public void onButtonInteraction(ButtonInteractionEvent event) {
        String id = event.getButton().getId();

        Submission submission;
        try {
            submission = ServerEssentials.database.cachedPlayerSubmissions.get(id);
        } catch (Exception e) {
            Bukkit.getLogger().info(e.toString());
            return;
        }

        String desc;
        try {
            desc = "Upvotes : " + submission.getUpvotes()
                + "\nComments : " + submission.getComments()
                + "\nAwards : " + submission.getAwards()
                + "\nCreated : " + submission.getCreationDate()
                + "\nSubreddit : " + submission.getSubreddit()
                + "\nArchived : " + submission.isArchived()
                + "\nNSFW : " + submission.isNSFW()
                + "\nSpoiler : " + submission.isSpoiler()
                + "\nCrosspostable : " + submission.isCrosspostable();
        } catch (IOException e) {
            return;
        }
        
        EmbedBuilder embed = new EmbedBuilder()
            .setDescription(desc)
            .setColor(new Color(0xe082ff));

        event.replyEmbeds(embed.build()).setEphemeral(true).queue();

            
        
    }
    
}
