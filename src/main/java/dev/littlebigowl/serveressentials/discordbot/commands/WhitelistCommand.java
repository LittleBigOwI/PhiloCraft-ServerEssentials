package dev.littlebigowl.serveressentials.discordbot.commands;

import javax.annotation.Nonnull;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.Modal;
import net.dv8tion.jda.api.interactions.components.text.TextInput;
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle;

public class WhitelistCommand extends ListenerAdapter{
    
    @Override
    public void onSlashCommandInteraction(@Nonnull SlashCommandInteractionEvent event) {

        if(event.getName().equals("whitelist")) {
            
            TextInput name = TextInput.create("whitelistInput", "Command", TextInputStyle.SHORT)
                .setPlaceholder("Enter command here...")
                .setRequired(true)
                .build();

            Modal modal = Modal.create("whitelistEdit", "Edit Whitelist")
                .addActionRows(ActionRow.of(name))
                .build();

            event.replyModal(modal).queue();

        }

    }

}
