package dev.littlebigowl.serveressentials.discordbot.events;

import java.sql.SQLException;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Statistic;
import org.bukkit.entity.Player;

import dev.littlebigowl.serveressentials.ServerEssentials;
import dev.littlebigowl.serveressentials.utils.TeamUtil;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.interaction.ModalInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.modals.ModalMapping;

public class OnModalSubmit extends ListenerAdapter {

    private final JDA bot;
    public OnModalSubmit(JDA bot) {
        this.bot = bot;
    }
    
    @Override
    public void onModalInteraction(@Nonnull ModalInteractionEvent event) {

        if(event.getModalId().equals("accountLink")) {

            ModalMapping code = event.getValue("codeInput");

            if(code == null) {
                return;
            }

            Boolean success = false;
            try {
                ServerEssentials.database.resetConnection();
                success = ServerEssentials.database.completeLink(Objects.requireNonNull(event.getMember()).getId(), code.getAsString());
            } catch(SQLException e) {
                Bukkit.getLogger().info(e.toString());
                return;
            }
            
            if (success) { 
                
                Member member = Objects.requireNonNull(event.getMember());
                String id = member.getId();
                Player player = Bukkit.getPlayer(ServerEssentials.database.cachedPlayerDiscords.getKey(id));
                
                int playtime;
                if(player == null) {
                    OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(ServerEssentials.database.cachedPlayerDiscords.getKey(id));
                    playtime = Math.round(offlinePlayer.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                } else {
                    playtime = Math.round(player.getStatistic(Statistic.PLAY_ONE_MINUTE)/1200);
                }
                String roleId = Objects.requireNonNull(ServerEssentials.database.playerRoles.get(TeamUtil.getTeamName(playtime)));
                String linkedRoleId = Objects.requireNonNull(ServerEssentials.database.playerRoles.get("linked"));

                member.getGuild().addRoleToMember(member, Objects.requireNonNull(this.bot.getRoleById(roleId))).queue();
                member.getGuild().addRoleToMember(member, Objects.requireNonNull(this.bot.getRoleById(linkedRoleId))).queue();

                event.reply("Successfully linked your discord account to your minecraft account, your role will be updated shortly.").setEphemeral(true).queue();

            } else {
                event.reply("Error : Code is either incorrect or account is already linked").setEphemeral(true).queue(); 
            }
        } 

    }

}
