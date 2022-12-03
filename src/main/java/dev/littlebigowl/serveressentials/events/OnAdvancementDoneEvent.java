package dev.littlebigowl.serveressentials.events;

import dev.littlebigowl.serveressentials.models.Config;
import dev.littlebigowl.serveressentials.utils.Colors;
import dev.littlebigowl.serveressentials.utils.ServerWebHook;
import dev.littlebigowl.serveressentials.utils.TeamUtil;

import java.util.HashMap;

import org.bukkit.Statistic;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;

public class OnAdvancementDoneEvent implements Listener {

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent event) {

        String advancementKey = event.getAdvancement().getKey().getKey();

        HashMap<String, String[]> advancemenMap = new HashMap<String, String[]>(){{
            
            //!OverWorld
            put("story/mine_stone", new String[]{"Stone Age","Mine stone with your new pickaxe"});
            put("story/upgrade_tools", new String[]{"Getting an Upgrade","Construct a better pickaxe"});
            put("story/smelt_iron", new String[]{"Acquire Hardware","Smelt an iron ingot"});
            put("story/obtain_armor", new String[]{"Suit Up","Protect yourself with a piece of iron armor"});
            put("story/lava_bucket", new String[]{"Hot Stuff","Fill a bucket with lava"});
            put("story/iron_tools", new String[]{"Isn't It Iron Pick","Upgrade your pickaxe"});
            put("story/deflect_arrow", new String[]{"Not Today, Thank You","Block a projectile using your shield"});
            put("story/form_obsidian", new String[]{"Ice Bucket Challenge","Obtain a block of obsidian"});
            put("story/mine_diamond", new String[]{"Diamonds!","Acquire diamonds"});
            put("story/enter_the_nether", new String[]{"We Need to Go Deeper","Build, light and enter a Nether Portal"});
            put("story/shiny_gear", new String[]{"Cover Me with Diamonds","Diamond armor saves lives"});
            put("story/enchant_item", new String[]{"Enchanter","Enchant an item at an Enchantment Table"});
            put("story/cure_zombie_villager", new String[]{"Zombie Doctor","Weaken and then cure a Zombie Villager"});
            put("story/follow_ender_eye", new String[]{"Eye Spy","Follow an Ender Eye"});
            put("story/enter_the_end", new String[]{"The End?","Enter the End Portal"});

            //!Nether
            put("nether/return_to_sender", new String[]{"Return to Sender","Destroy a Ghast with a fireball"});
            put("nether/find_bastion", new String[]{"Those Were the Days","Enter a Bastion Remnant"});
            put("Hidden in the Depths", new String[]{"Hidden in the Depths","Obtain Ancient Debris"});
            put("nether/fast_travel", new String[]{"Subspace Bubble","Use the Nether to travel 7 km in the Overworld"});
            put("nether/find_fortress", new String[]{"A Terrible Fortress","Break your way into a Nether Fortress"});
            put("nether/obtain_crying_obsidian", new String[]{"Who is Cutting Onions?","Obtain Crying Obsidian"});
            put("nether/distract_piglin", new String[]{"Oh Shiny","Distract Piglins with gold"});
            put("nether/ride_strider", new String[]{"This Boat Has Legs","Ride a Strider with a Warped Fungus on a Stick"});
            put("nether/uneasy_alliance", new String[]{"Uneasy Alliance","Rescue a Ghast from the Nether, bring it safely home to the Overworld... and then kill it"});
            put("nether/loot_bastion", new String[]{"War Pigs","Loot a chest in a Bastion Remnant"});
            put("nether/use_lodestone", new String[]{"Country Lode, Take Me Home","Use a compass on a Lodestone"});
            put("nether/netherite_armor", new String[]{"Cover Me in Debris","Get a full suit of Netherite armor"});
            put("nether/get_wither_skull", new String[]{"Spooky Scary Skeleton","Obtain a Wither Skeleton's skull"});
            put("nether/obtain_blaze_rod", new String[]{"Into Fire","Relieve a Blaze of its rod"});
            put("nether/charge_respawn_anchor", new String[]{"Not Quite \"Nine\" Lives","Charge a Respawn Anchor to the maximum"});
            put("nether/ride_strider_in_overworld_lava", new String[]{"Feels Like Home","Take a Strider for a loooong ride on a lava lake in the Overworld"});
            put("nether/explore_nether", new String[]{"Hot Tourist Destinations","Explore all Nether biomes"});
            put("nether/summon_wither", new String[]{"Withering Heights","Summon the Wither"});
            put("nether/brew_potion", new String[]{"Local Brewery","Brew a potion"});
            put("nether/create_beacon", new String[]{"Bring Home the Beacon","Construct and place a beacon"});
            put("nether/all_potions", new String[]{"A Furious Cocktail","Have every potion effect applied at the same time"});
            put("nether/create_full_beacon", new String[]{"Beaconator","Bring Home the Beacon"});
            put("nether/all_effects", new String[]{"How Did We Get Here?","A Furious Cocktail"});
            
            //!End
            put("end/kill_dragon", new String[]{"Free the End","Good luck"});
            put("end/dragon_egg", new String[]{"The Next Generation","Hold the Dragon Egg"});
            put("end/enter_end_gateway", new String[]{"Remote Getaway","Escape the island"});
            put("end/respawn_dragon", new String[]{"The End... Again...","Respawn the Ender Dragon"});
            put("end/dragon_breath", new String[]{"You Need a Mint","Collect dragon's breath in a glass bottle"});
            put("end/find_end_city", new String[]{"The City at the End of the Game","Go on in, what could happen?"});
            put("end/elytra", new String[]{"Sky's the Limit","Find elytra"});
            put("end/levitate", new String[]{"Great View From Up Here","Levitate up 50 blocks from the attacks of a Shulker"});

            //!Adventure
            put("adventure/voluntary_exile", new String[]{"Voluntary Exile","Maybe consider staying away from villages for the time being..."});
            put("adventure/spyglass_at_parrot", new String[]{"Is It a Bird?","Look at a parrot through a spyglass"});
            put("adventure/kill_a_mob", new String[]{"Monster Hunter","Kill any hostile monster"});
            put("adventure/trade", new String[]{"What a Deal!","Successfully trade with a Villager"});
            put("adventure/honey_block_slide", new String[]{"Sticky Situation","Jump into a Honey Block to break your fall"});
            put("adventure/ol_betsy", new String[]{"Ol' Betsy","Shoot a crossbow"});
            put("adventure/lightning_rod_with_villager_no_fire", new String[]{"Surge Protector","Protect a villager from an undesired shock without starting a fire"});
            put("adventure/fall_from_world_height", new String[]{"Caves & Cliffs","Free fall from the top of the world (build limit) to the bottom of the world and survive"});
            put("adventure/avoid_vibration", new String[]{"Sneak 100","Sneak near a Sculk Sensor or Warden to prevent it from detecting you"});
            put("adventure/sleep_in_bed", new String[]{"Sweet Dreams","Sleep in a bed to change your respawn point"});
            put("adventure/hero_of_the_village", new String[]{"Hero of the Village","Successfully defend a village from a raid"});
            put("adventure/spyglass_at_ghast", new String[]{"Is It a Balloon?","Look at a ghast through a spyglass"});
            put("adventure/throw_trident", new String[]{"A Throwaway Joke","Throw a trident at something."});
            put("adventure/kill_mob_near_sculk_catalyst", new String[]{"It Spreads","Kill a mob near a Sculk Catalyst"});
            put("adventure/shoot_arrow", new String[]{"Take Aim","Shoot something with an arrow"});
            put("adventure/kill_all_mobs", new String[]{"Monsters Hunted","Kill one of every hostile monster"});
            put("adventure/totem_of_undying", new String[]{"Postmortal","Use a Totem of Undying to cheat death"});
            put("adventure/summon_iron_golem", new String[]{"Hired Help","Summon an Iron Golem to help defend a village"});
            put("adventure/trade_at_world_height", new String[]{"Star Trader","Trade with a Villager at the build height limit"});
            put("adventure/two_birds_one_arrow", new String[]{"Two Birds, One Arrow","Kill two Phantoms with a piercing arrow"});
            put("adventure/whos_the_pillager_now", new String[]{"Who's the Pillager Now?","Give a Pillager a taste of their own medicine"});
            put("adventure/arbalistic", new String[]{"Arbalistic","Kill five unique mobs with one crossbow shot"});
            put("adventure/adventuring_time", new String[]{"Adventuring Time","Discover every biome"});
            put("adventure/play_jukebox_in_meadows", new String[]{"Sound of Music","Make the Meadows come alive with the sound of music from a Jukebox"});
            put("adventure/walk_on_powder_snow_with_leather_boots", new String[]{"Light as a Rabbit","Walk on powder snow...without sinking in it"});
            put("adventure/spyglass_at_dragon", new String[]{"Is It a Plane?","Look at the Ender Dragon through a spyglass"});
            put("adventure/very_very_frightening", new String[]{"Very Very Frightening","Strike a Villager with lightning"});
            put("adventure/sniper_duel", new String[]{"Sniper Duel","Kill a Skeleton from at least 50 meters away"});
            put("adventure/bullseye", new String[]{"Bullseye","Hit the bullseye of a Target block from at least 30 meters away"});
            
            //!Husbandry
            put("husbandry/safely_harvest_honey", new String[]{"Bee Our Guest","Use a Campfire to collect Honey from a Beehive using a Bottle without aggravating the bees"});
            put("husbandry/breed_an_animal", new String[]{"The Parrots and the Bats","Breed two animals together"});
            put("husbandry/allay_deliver_item_to_player", new String[]{"You've Got a Friend in Me","Have an Allay deliver items to you"});
            put("husbandry/ride_a_boat_with_a_goat", new String[]{"Whatever Floats Your Goat!","Get in a Boat and float with a Goat"});
            put("husbandry/tame_an_animal", new String[]{"Best Friends Forever","Tame an animal"});
            put("husbandry/make_a_sign_glow", new String[]{"Glow and Behold!","Make the text of any sign glow"});
            put("husbandry/fishy_business", new String[]{"Fishy Business","Catch a fish"});
            put("husbandry/silk_touch_nest", new String[]{"Total Beelocation","Move a Bee Nest, with 3 bees inside, using Silk Touch"});
            put("husbandry/tadpole_in_a_bucket", new String[]{"Bukkit Bukkit","Catch a Tadpole in a Bucket"});
            put("husbandry/plant_seed", new String[]{"A Seedy Place","Plant a seed and watch it grow"});
            put("husbandry/wax_on", new String[]{"Wax On","Apply Honeycomb to a Copper block!"});
            put("husbandry/bred_all_animals", new String[]{"Two by Two","Breed all the animals!"});
            put("husbandry/allay_deliver_cake_to_note_block", new String[]{"Birthday Song","Have an Allay drop a Cake at a Note Block"});
            put("husbandry/complete_catalogue", new String[]{"A Complete Catalogue","Tame all cat variants!"});
            put("husbandry/tactical_fishing", new String[]{"Tactical Fishing","Catch a fish... without a fishing rod!"});
            put("husbandry/leash_all_frog_variants", new String[]{"When the Squad Hops into Town","Get each Frog variant on a Lead"});
            put("husbandry/balanced_diet", new String[]{"A Balanced Diet","Eat everything that is edible, even if it's not good for you"});
            put("husbandry/obtain_netherite_hoe", new String[]{"Serious Dedication","Use a Netherite Ingot to upgrade a hoe, and then reevaluate your life choices"});
            put("husbandry/wax_off", new String[]{"Wax Off","Scrape Wax off of a Copper block!"});
            put("husbandry/axolotl_in_a_bucket", new String[]{"The Cutest Predator","Catch an axolotl in a bucket"});
            put("husbandry/froglights", new String[]{"With Our Powers Combined!","Have all Froglights in your inventory"});
            put("husbandry/kill_axolotl_target", new String[]{"The Healing Power of Friendship!","Team up with an axolotl and win a fight"});
        }};

        String advancementTitle;
        String advancementDescription;
        try {
            advancementTitle = advancemenMap.get(advancementKey)[0];
            advancementDescription = advancemenMap.get(advancementKey)[1];
        } catch(Exception e) {
            return;
        }

        ServerWebHook serverWebHook = new ServerWebHook(
            Config.get().getString("DiscordWebhookURL"),
            "Server",
            Config.get().getString("DiscordWebhookAvatarURL")
        );
        serverWebHook.sendEmbed(Colors.ADVANCEMENT, TeamUtil.getTeamPrefix(Math.round(event.getPlayer().getStatistic(Statistic.PLAY_ONE_MINUTE)/1200)) + " " + event.getPlayer().getName() +" has made the advancement [" + advancementTitle +"]", "https://minotar.net/avatar/" + event.getPlayer().getName() +".png", advancementDescription);
    }

}
