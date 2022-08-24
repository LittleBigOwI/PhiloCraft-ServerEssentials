package dev.littlebigowl.serveressentials.events;

import club.minnced.discord.webhook.WebhookClient;
import club.minnced.discord.webhook.WebhookClientBuilder;
import club.minnced.discord.webhook.send.WebhookEmbed;
import club.minnced.discord.webhook.send.WebhookEmbedBuilder;
import club.minnced.discord.webhook.send.WebhookMessage;
import club.minnced.discord.webhook.send.WebhookMessageBuilder;
import dev.littlebigowl.serveressentials.models.Config;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class OnAdvancementDoneEvent implements Listener {

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {

        String advancementResourceLocation = event.getAdvancement().getKey().getKey();

        Integer color = null;
        String advancementDisplayName;
        switch (advancementResourceLocation) {
            case "story/mine_stone" : advancementDisplayName = "Stone Age"; break;
            case "story/upgrade_tools" : advancementDisplayName = "Getting an Upgrade"; break;
            case "story/smelt_iron" : advancementDisplayName = "Acquire Hardware"; break;
            case "story/obtain_armor" : advancementDisplayName = "Suit Up"; break;
            case "story/lava_bucket" : advancementDisplayName = "Hot Stuff"; break;
            case "story/iron_tools" : advancementDisplayName = "Isn't It Iron Pick"; break;
            case "story/deflect_arrow" : advancementDisplayName = "Not Today, Thank You"; break;
            case "story/form_obsidian" : advancementDisplayName = "Ice Bucket Challenge"; break;
            case "story/mine_diamond" : advancementDisplayName = "Diamonds!"; break;
            case "story/enter_the_nether" : advancementDisplayName = "We Need to Go Deeper"; break;
            case "story/shiny_gear" : advancementDisplayName = "Cover Me with Diamonds"; break;
            case "story/enchant_item" : advancementDisplayName = "Enchanter"; break;
            case "story/cure_zombie_villager" : advancementDisplayName = "Zombie Doctor"; break;
            case "story/follow_ender_eye" : advancementDisplayName = "Eye Spy"; break;
            case "story/enter_the_end" : advancementDisplayName = "The End?"; break;

            case "nether/return_to_sender" : advancementDisplayName = "Return to Sender"; color = 0xa800a8; break;
            case "nether/find_bastion" : advancementDisplayName = "Those Were the Days"; break;
            case "nether/obtain_ancient_debris" : advancementDisplayName = "Hidden in the Depths"; break;
            case "nether/fast_travel" : advancementDisplayName = "Subspace Bubble"; color = 0xa800a8; break;
            case "nether/find_fortress" : advancementDisplayName = "A Terrible Fortress"; break;
            case "nether/obtain_crying_obsidian" : advancementDisplayName = "Who is Cutting Onions?"; break;
            case "nether/distract_piglin" : advancementDisplayName = "Oh Shiny"; break;
            case "nether/ride_strider" : advancementDisplayName = "This Boat Has Legs"; break;
            case "nether/uneasy_alliance" : advancementDisplayName = "Uneasy Alliance"; color = 0xa800a8; break;
            case "nether/loot_bastion" : advancementDisplayName = "War Pigs"; break;
            case "nether/use_lodestone" : advancementDisplayName = "Country Lode, Take Me Home"; break;
            case "nether/netherite_armor" : advancementDisplayName = "Cover Me in Debris"; color = 0xa800a8; break;
            case "nether/get_wither_skull" : advancementDisplayName = "Spooky Scary Skeleton"; break;
            case "nether/obtain_blaze_rod" : advancementDisplayName = "Into Fire"; break;
            case "nether/charge_respawn_anchor" : advancementDisplayName = "Not Quite \"Nine\" Lives"; break;
            case "nether/ride_strider_in_overworld_lava" : advancementDisplayName = "Feels Like Home"; break;
            case "nether/explore_nether" : advancementDisplayName = "Hot Tourist Destinations"; color = 0xa800a8; break;
            case "nether/summon_wither" : advancementDisplayName = "Withering Heights"; break;
            case "nether/brew_potion" : advancementDisplayName = "Local Brewery"; break;
            case "nether/create_beacon" : advancementDisplayName = "Bring Home the Beacon"; break;
            case "nether/all_potions" : advancementDisplayName = "A Furious Cocktail"; color = 0xa800a8; break;
            case "nether/create_full_beacon" : advancementDisplayName = "Beaconator"; break;
            case "nether/all_effects" : advancementDisplayName = "How Did We Get Here?"; color = 0xa800a8; break;

            case "end/kill_dragon" : advancementDisplayName = "Free the End"; break;
            case "end/dragon_egg" : advancementDisplayName = "The Next Generation"; break;
            case "end/enter_end_gateway" : advancementDisplayName = "Remote Getaway"; break;
            case "end/respawn_dragon" : advancementDisplayName = "The End... Again..."; break;
            case "end/dragon_breath" : advancementDisplayName = "You Need a Mint"; break;
            case "end/find_end_city" : advancementDisplayName = "The City at the End of the Game"; break;
            case "end/elytra" : advancementDisplayName = "Sky's the Limit"; break;
            case "end/levitate" : advancementDisplayName = "Great View From Up Here"; color = 0xa800a8; break;

            case "adventure/voluntary_exile" : advancementDisplayName = "Voluntary Exile"; break;
            case "adventure/spyglass_at_parrot" : advancementDisplayName = "Is It a Bird?"; break;
            case "adventure/kill_a_mob" : advancementDisplayName = "Monster Hunter"; break;
            case "adventure/trade" : advancementDisplayName = "What a Deal!"; break;
            case "adventure/honey_block_slide" : advancementDisplayName = "Sticky Situation"; break;
            case "adventure/ol_betsy" : advancementDisplayName = "Ol' Betsy"; break;
            case "adventure/lightning_rod_with_villager_no_fire" : advancementDisplayName = "Surge Protector"; break;
            case "adventure/fall_from_world_height" : advancementDisplayName = "Caves & Cliffs"; break;
            case "adventure/avoid_vibration" : advancementDisplayName = "Sneak 100"; break;
            case "adventure/sleep_in_bed" : advancementDisplayName = "Sweet Dreams"; break;
            case "adventure/hero_of_the_village" : advancementDisplayName = "Hero of the Village"; color = 0xa800a8; break;
            case "adventure/spyglass_at_ghast" : advancementDisplayName = "Is It a Balloon?"; break;
            case "adventure/throw_trident" : advancementDisplayName = "A Throwaway Joke"; break;
            case "adventure/kill_mob_near_sculk_catalyst" : advancementDisplayName = "It Spreads"; color = 0xa800a8; break;
            case "adventure/shoot_arrow" : advancementDisplayName = "Take Aim"; break;
            case "adventure/kill_all_mobs" : advancementDisplayName = "Monsters Hunted"; color = 0xa800a8; break;
            case "adventure/totem_of_undying" : advancementDisplayName = "Postmortal"; break;
            case "adventure/summon_iron_golem" : advancementDisplayName = "Hired Help"; break;
            case "adventure/trade_at_world_height" : advancementDisplayName = "Star Trader"; break;
            case "adventure/two_birds_one_arrow" : advancementDisplayName = "Two Birds, One Arrow"; color = 0xa800a8; break;
            case "adventure/whos_the_pillager_now" : advancementDisplayName = "Who's the Pillager Now?"; break;
            case "adventure/arbalistic" : advancementDisplayName = "Arbalistic"; color = 0xa800a8; break;
            case "adventure/adventuring_time" : advancementDisplayName = "Adventuring Time"; color = 0xa800a8; break;
            case "adventure/play_jukebox_in_meadows" : advancementDisplayName = "Sound of Music"; break;
            case "adventure/walk_on_powder_snow_with_leather_boots" : advancementDisplayName = "Light as a Rabbit"; break;
            case "adventure/spyglass_at_dragon" : advancementDisplayName = "Is It a Plane?"; break;
            case "adventure/very_very_frightening" : advancementDisplayName = "Very Very Frightening"; break;
            case "adventure/sniper_duel" : advancementDisplayName = "Sniper Duel"; color = 0xa800a8; break;
            case "adventure/bullseye" : advancementDisplayName = "Bullseye"; color = 0xa800a8; break;

            case "husbandry/safely_harvest_honey" : advancementDisplayName = "Bee Our Guest"; break;
            case "husbandry/breed_an_animal" : advancementDisplayName = "The Parrots and the Bats"; break;
            case "husbandry/allay_deliver_item_to_player" : advancementDisplayName = "You've Got a Friend in Me"; break;
            case "husbandry/ride_a_boat_with_a_goat" : advancementDisplayName = "Whatever Floats Your Goat!"; break;
            case "husbandry/tame_an_animal" : advancementDisplayName = "Best Friends Forever"; break;
            case "husbandry/make_a_sign_glow" : advancementDisplayName = "Glow and Behold!"; break;
            case "husbandry/fishy_business" : advancementDisplayName = "Fishy Business"; break;
            case "husbandry/silk_touch_nest" : advancementDisplayName = "Total Beelocation"; break;
            case "husbandry/tadpole_in_a_bucket" : advancementDisplayName = "Bukkit Bukkit"; break;
            case "husbandry/plant_seed" : advancementDisplayName = "A Seedy Place"; break;
            case "husbandry/wax_on" : advancementDisplayName = "Wax On"; break;
            case "husbandry/bred_all_animals" : advancementDisplayName = "Two by Two"; color = 0xa800a8; break;
            case "husbandry/allay_deliver_cake_to_note_block" : advancementDisplayName = "Birthday Song"; color = 0xa800a8; break;
            case "husbandry/complete_catalogue" : advancementDisplayName = "A Complete Catalogue"; color = 0xa800a8; break;
            case "husbandry/tactical_fishing" : advancementDisplayName = "Tactical Fishing"; break;
            case "husbandry/leash_all_frog_variants" : advancementDisplayName = "When the Squad Hops into Town"; break;
            case "husbandry/balanced_diet" : advancementDisplayName = "A Balanced Diet"; color = 0xa800a8; break;
            case "husbandry/obtain_netherite_hoe" : advancementDisplayName = "Serious Dedication"; color = 0xa800a8; break;
            case "husbandry/wax_off" : advancementDisplayName = "Wax Off"; break;
            case "husbandry/axolotl_in_a_bucket" : advancementDisplayName = "The Cutest Predator"; break;
            case "husbandry/froglights" : advancementDisplayName = "With Our Powers Combined!"; color = 0xa800a8; break;
            case "husbandry/kill_axolotl_target" : advancementDisplayName = "The Healing Power of Friendship!"; break;

            default : advancementDisplayName = null; break;
        }

        if (color == null) {
            color = 0x54fb54;
        }

        if (advancementDisplayName == null) {
            return;
        }

        WebhookMessageBuilder builder = new WebhookMessageBuilder();
        builder.addEmbeds(new WebhookEmbedBuilder().setColor(color).setAuthor(new WebhookEmbed.EmbedAuthor(event.getPlayer().getName() + " has made the advancement [" + advancementDisplayName + "]", "https://minotar.net/avatar/" + event.getPlayer().getName() + ".png", "")).build());
        builder.setAvatarUrl("https://preview.redd.it/1wo65al6iox71.png?width=640&crop=smart&auto=webp&s=e9aab23333f9556cbeaa37587002dc9d7181137f");
        builder.setUsername("PhiloCraft");

        WebhookMessage message = builder.build();

        WebhookClientBuilder webBuilder = new WebhookClientBuilder(Config.get().getString("DiscordWebhookURL"));
        WebhookClient client = webBuilder.build();
        client.send(message);

    }

}
