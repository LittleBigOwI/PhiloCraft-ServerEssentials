package dev.littlebigowl.serveressentials.discordbot.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class LinkCommand extends ListenerAdapter {
    
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("link")) {
            
            TextInput name = TextInput.create("codeInput", "Code", TextInputStyle.SHORT)
                .setMinLength(8)
                .setMaxLength(12)
                .setPlaceholder("Enter the generated code here...")
                .setRequired(true)
                .build();

            Modal modal = Modal.create("accountLink", "Link your account")
                .addActionRows(ActionRow.of(name))
                .build();

            event.replyModal(modal).queue();
        }

    }
    
}
