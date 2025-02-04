package Events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class Event4 extends ListenerAdapter {
    private static final List<Pokemon> POKEMON_LIST = new ArrayList<>();
    private final Map<String, Pokemon> playerPicks = new HashMap<>();
    private String challengerId = null;
    private String challengedUserId = null;
    private String middlemanId = null;
    private boolean challengeActive = false;
    private boolean challengeConfirmed = false;

    private static final List<String> ALLOWED_CHANNEL_IDS = Arrays.asList("1323664415618236416", "1323664289755824260");
    private static final List<String> MIDDLEMAN_IDS = Arrays.asList("700580614029574235", "767386032370089995","360815908274438148");

    static {
        POKEMON_LIST.add(new Pokemon("Charizard", 78, 84, 78, 109, 85, 100));
        POKEMON_LIST.add(new Pokemon("Blastoise", 79, 83, 100, 85, 105, 78));
        POKEMON_LIST.add(new Pokemon("Venusaur", 80, 82, 83, 100, 100, 80));

        POKEMON_LIST.add(new Pokemon("Butterfree", 60, 45, 50, 90, 80, 70));
        POKEMON_LIST.add(new Pokemon("Beedrill", 65, 90, 40, 45, 80, 75));
        POKEMON_LIST.add(new Pokemon("Pidgeot", 83, 80, 75, 70, 70, 101));
        POKEMON_LIST.add(new Pokemon("Raticate", 55, 81, 60, 50, 70, 97));
        POKEMON_LIST.add(new Pokemon("Fearow", 65, 90, 65, 61, 61, 100));
        POKEMON_LIST.add(new Pokemon("Arbok", 60, 95, 69, 65, 79, 80));
        POKEMON_LIST.add(new Pokemon("Raichu", 60, 90, 55, 90, 80, 110));
        POKEMON_LIST.add(new Pokemon("Sandslash", 75, 100, 110, 45, 55, 65));
        POKEMON_LIST.add(new Pokemon("Nidoqueen", 90, 92, 87, 75, 85, 76));
        POKEMON_LIST.add(new Pokemon("Nidoking", 81, 102, 77, 85, 75, 85));
        POKEMON_LIST.add(new Pokemon("Clefable", 95, 70, 73, 95, 90, 60));
        POKEMON_LIST.add(new Pokemon("Ninetales", 73, 76, 75, 81, 100, 100));
        POKEMON_LIST.add(new Pokemon("Wigglytuff", 140, 70, 45, 75, 50, 45));
        POKEMON_LIST.add(new Pokemon("Vileplume", 75, 80, 85, 110, 90, 50));
        POKEMON_LIST.add(new Pokemon("Parasect", 60, 95, 80, 60, 80, 30));
        POKEMON_LIST.add(new Pokemon("Venomoth", 70, 65, 60, 90, 75, 90));
        POKEMON_LIST.add(new Pokemon("Dugtrio", 35, 100, 50, 50, 70, 120));
        POKEMON_LIST.add(new Pokemon("Persian", 65, 70, 60, 65, 65, 115));
        POKEMON_LIST.add(new Pokemon("Golduck", 80, 82, 78, 95, 80, 85));
        POKEMON_LIST.add(new Pokemon("Primeape", 65, 105, 60, 60, 70, 95));
        POKEMON_LIST.add(new Pokemon("Arcanine", 90, 110, 80, 100, 80, 95));
        POKEMON_LIST.add(new Pokemon("Poliwrath", 90, 95, 95, 70, 90, 70));
        POKEMON_LIST.add(new Pokemon("Alakazam", 55, 50, 45, 135, 85, 120));
        POKEMON_LIST.add(new Pokemon("Machamp", 90, 130, 80, 65, 85, 55));
        POKEMON_LIST.add(new Pokemon("Victreebel", 80, 105, 65, 100, 70, 70));
        POKEMON_LIST.add(new Pokemon("Tentacruel", 80, 70, 65, 80, 120, 100));
        POKEMON_LIST.add(new Pokemon("Golem", 80, 120, 130, 55, 65, 45));
        POKEMON_LIST.add(new Pokemon("Rapidash", 65, 100, 70, 80, 80, 105));
        POKEMON_LIST.add(new Pokemon("Slowbro", 95, 75, 110, 100, 80, 30));
        POKEMON_LIST.add(new Pokemon("Magneton", 50, 60, 95, 120, 70, 70));
        POKEMON_LIST.add(new Pokemon("Farfetchd", 52, 65, 55, 58, 62, 60));
        POKEMON_LIST.add(new Pokemon("Dodrio", 60, 110, 70, 60, 60, 110));
        POKEMON_LIST.add(new Pokemon("Dewgong", 90, 70, 80, 70, 95, 70));
        POKEMON_LIST.add(new Pokemon("Muk", 105, 105, 75, 65, 100, 50));
        POKEMON_LIST.add(new Pokemon("Cloyster", 50, 95, 180, 85, 45, 70));
        POKEMON_LIST.add(new Pokemon("Gengar", 60, 65, 60, 130, 75, 110));
        POKEMON_LIST.add(new Pokemon("Hypno", 85, 73, 70, 73, 115, 67));
        POKEMON_LIST.add(new Pokemon("Kingler", 55, 130, 115, 50, 50, 75));
        POKEMON_LIST.add(new Pokemon("Electrode", 60, 50, 70, 80, 80, 150));
        POKEMON_LIST.add(new Pokemon("Exeggutor", 95, 95, 85, 125, 75, 55));
        POKEMON_LIST.add(new Pokemon("Marowak", 60, 80, 110, 50, 80, 45));
        POKEMON_LIST.add(new Pokemon("Hitmonlee", 50, 120, 53, 35, 110, 87));
        POKEMON_LIST.add(new Pokemon("Hitmonchan", 50, 105, 79, 35, 110, 76));
        POKEMON_LIST.add(new Pokemon("Lickitung", 90, 55, 75, 60, 75, 30));
        POKEMON_LIST.add(new Pokemon("Weezing", 65, 90, 120, 85, 70, 60));
        POKEMON_LIST.add(new Pokemon("Rhydon", 105, 130, 120, 45, 45, 40));
        POKEMON_LIST.add(new Pokemon("Chansey", 250, 5, 5, 35, 105, 50));
        POKEMON_LIST.add(new Pokemon("Tangela", 65, 55, 115, 100, 40, 60));
        POKEMON_LIST.add(new Pokemon("Kangaskhan", 105, 95, 80, 40, 80, 90));
        POKEMON_LIST.add(new Pokemon("Seadra", 55, 65, 95, 95, 45, 85));
        POKEMON_LIST.add(new Pokemon("Seaking", 80, 92, 65, 65, 80, 68));
        POKEMON_LIST.add(new Pokemon("Starmie", 60, 75, 85, 100, 85, 115));
        POKEMON_LIST.add(new Pokemon("Mr. Mime", 40, 45, 65, 100, 120, 90));
        POKEMON_LIST.add(new Pokemon("Scyther", 70, 110, 80, 55, 80, 105));
        POKEMON_LIST.add(new Pokemon("Jynx", 65, 50, 35, 115, 95, 95));
        POKEMON_LIST.add(new Pokemon("Electabuzz", 65, 83, 57, 95, 85, 105));
        POKEMON_LIST.add(new Pokemon("Magmar", 65, 95, 57, 100, 85, 93));
        POKEMON_LIST.add(new Pokemon("Pinsir", 65, 125, 100, 55, 70, 85));
        POKEMON_LIST.add(new Pokemon("Tauros", 75, 100, 95, 40, 70, 110));
        POKEMON_LIST.add(new Pokemon("Gyarados", 95, 125, 79, 60, 100, 81));
        POKEMON_LIST.add(new Pokemon("Lapras", 130, 85, 80, 85, 95, 60));
        POKEMON_LIST.add(new Pokemon("Ditto", 48, 48, 48, 48, 48, 48));
        POKEMON_LIST.add(new Pokemon("Vaporeon", 130, 65, 60, 110, 95, 65));
        POKEMON_LIST.add(new Pokemon("Jolteon", 65, 65, 60, 110, 95, 130));
        POKEMON_LIST.add(new Pokemon("Flareon", 65, 130, 60, 95, 110, 65));
        POKEMON_LIST.add(new Pokemon("Jolteon", 65, 65, 60, 110, 95, 130));
        POKEMON_LIST.add(new Pokemon("Vaporeon", 130, 65, 60, 110, 95, 65));
        POKEMON_LIST.add(new Pokemon("Omastar", 70, 60, 125, 115, 70, 55));
        POKEMON_LIST.add(new Pokemon("Kabutops", 60, 115, 105, 65, 70, 80));
        POKEMON_LIST.add(new Pokemon("Aerodactyl", 80, 105, 65, 60, 75, 130));
        POKEMON_LIST.add(new Pokemon("Snorlax", 160, 110, 65, 65, 110, 30));
        POKEMON_LIST.add(new Pokemon("Articuno", 90, 85, 100, 95, 125, 85));
        POKEMON_LIST.add(new Pokemon("Zapdos", 90, 90, 85, 125, 90, 100));
        POKEMON_LIST.add(new Pokemon("Moltres", 90, 100, 90, 125, 85, 90));
        POKEMON_LIST.add(new Pokemon("Dragonair", 61, 84, 65, 70, 70, 70));
        POKEMON_LIST.add(new Pokemon("Dragonite", 91, 134, 95, 100, 100, 80));
        POKEMON_LIST.add(new Pokemon("Mewtwo", 106, 110, 90, 154, 90, 130));
        POKEMON_LIST.add(new Pokemon("Mew", 100, 100, 100, 100, 100, 100));

        POKEMON_LIST.add(new Pokemon("Meganium", 80, 82, 100, 83, 100, 80));
        POKEMON_LIST.add(new Pokemon("Typhlosion", 78, 84, 78, 109, 85, 100));
        POKEMON_LIST.add(new Pokemon("Feraligatr", 85, 105, 100, 79, 83, 78));
        POKEMON_LIST.add(new Pokemon("Noctowl", 100, 50, 50, 76, 96, 70));
        POKEMON_LIST.add(new Pokemon("Ledian", 55, 35, 50, 55, 110, 85));
        POKEMON_LIST.add(new Pokemon("Ariados", 70, 90, 70, 60, 70, 40));
        POKEMON_LIST.add(new Pokemon("Crobat", 85, 90, 80, 70, 80, 130));
        POKEMON_LIST.add(new Pokemon("Lanturn", 125, 58, 58, 76, 76, 67));
        POKEMON_LIST.add(new Pokemon("Xatu", 65, 75, 70, 95, 70, 95));
        POKEMON_LIST.add(new Pokemon("Ampharos", 90, 75, 85, 115, 90, 55));
        POKEMON_LIST.add(new Pokemon("Bellossom", 75, 80, 95, 90, 100, 50));
        POKEMON_LIST.add(new Pokemon("Azumarill", 100, 50, 80, 60, 80, 50));
        POKEMON_LIST.add(new Pokemon("Sudowoodo", 70, 100, 115, 30, 65, 30));
        POKEMON_LIST.add(new Pokemon("Politoed", 90, 75, 75, 90, 100, 70));
        POKEMON_LIST.add(new Pokemon("Jumpluff", 75, 55, 70, 55, 95, 110));
        POKEMON_LIST.add(new Pokemon("Sunflora", 75, 75, 55, 105, 85, 30));
        POKEMON_LIST.add(new Pokemon("Quagsire", 95, 85, 85, 65, 65, 35));
        POKEMON_LIST.add(new Pokemon("Espeon", 65, 65, 60, 130, 95, 110));
        POKEMON_LIST.add(new Pokemon("Umbreon", 95, 65, 110, 60, 130, 65));
        POKEMON_LIST.add(new Pokemon("Slowking", 95, 75, 80, 100, 110, 30));
        POKEMON_LIST.add(new Pokemon("Forretress", 75, 90, 140, 60, 60, 40));
        POKEMON_LIST.add(new Pokemon("Steelix", 75, 85, 200, 55, 65, 30));
        POKEMON_LIST.add(new Pokemon("Granbull", 90, 120, 75, 60, 60, 45));
        POKEMON_LIST.add(new Pokemon("Qwilfish", 65, 95, 85, 55, 55, 85));
        POKEMON_LIST.add(new Pokemon("Scizor", 70, 130, 100, 55, 80, 65));
        POKEMON_LIST.add(new Pokemon("Shuckle", 20, 10, 230, 10, 230, 5));
        POKEMON_LIST.add(new Pokemon("Heracross", 80, 125, 75, 40, 95, 85));
        POKEMON_LIST.add(new Pokemon("Ursaring", 90, 130, 75, 75, 75, 55));
        POKEMON_LIST.add(new Pokemon("Magcargo", 50, 50, 120, 80, 80, 30));
        POKEMON_LIST.add(new Pokemon("Piloswine", 100, 100, 80, 60, 60, 50));
        POKEMON_LIST.add(new Pokemon("Octillery", 75, 105, 75, 105, 75, 45));
        POKEMON_LIST.add(new Pokemon("Delibird", 45, 55, 45, 65, 45, 75));
        POKEMON_LIST.add(new Pokemon("Mantine", 85, 40, 70, 80, 140, 70));
        POKEMON_LIST.add(new Pokemon("Skarmory", 65, 80, 140, 40, 70, 70));
        POKEMON_LIST.add(new Pokemon("Houndoom", 75, 90, 50, 110, 80, 95));
        POKEMON_LIST.add(new Pokemon("Kingdra", 75, 95, 95, 95, 95, 85));
        POKEMON_LIST.add(new Pokemon("Donphan", 90, 120, 120, 60, 60, 50));
        POKEMON_LIST.add(new Pokemon("Porygon2", 85, 80, 90, 105, 95, 60));
        POKEMON_LIST.add(new Pokemon("Stantler", 73, 95, 62, 85, 65, 85));
        POKEMON_LIST.add(new Pokemon("Smeargle", 55, 20, 35, 20, 45, 75));
        POKEMON_LIST.add(new Pokemon("Hitmontop", 50, 95, 95, 35, 110, 70));
        POKEMON_LIST.add(new Pokemon("Miltank", 95, 80, 105, 40, 70, 100));
        POKEMON_LIST.add(new Pokemon("Blissey", 255, 10, 10, 75, 135, 55));
        POKEMON_LIST.add(new Pokemon("Raikou", 90, 85, 75, 115, 100, 115));
        POKEMON_LIST.add(new Pokemon("Entei", 115, 115, 85, 90, 75, 100));
        POKEMON_LIST.add(new Pokemon("Suicune", 100, 75, 115, 90, 115, 85));
        POKEMON_LIST.add(new Pokemon("Tyranitar", 100, 134, 110, 95, 100, 61));
        POKEMON_LIST.add(new Pokemon("Lugia", 106, 90, 130, 90, 154, 110));
        POKEMON_LIST.add(new Pokemon("Ho-oh", 106, 130, 90, 110, 154, 90));
        POKEMON_LIST.add(new Pokemon("Celebi", 100, 100, 100, 100, 100, 100));

        POKEMON_LIST.add(new Pokemon("Sceptile", 70, 85, 65, 105, 85, 120));
        POKEMON_LIST.add(new Pokemon("Blaziken", 80, 120, 70, 110, 70, 80));
        POKEMON_LIST.add(new Pokemon("Swampert", 100, 110, 90, 85, 90, 60));
        POKEMON_LIST.add(new Pokemon("Mightyena", 70, 90, 70, 60, 60, 70));
        POKEMON_LIST.add(new Pokemon("Linoone", 78, 70, 61, 50, 61, 100));
        POKEMON_LIST.add(new Pokemon("Beautifly", 60, 70, 50, 100, 50, 65));
        POKEMON_LIST.add(new Pokemon("Dustox", 60, 50, 70, 50, 90, 65));
        POKEMON_LIST.add(new Pokemon("Ludicolo", 80, 70, 70, 90, 100, 70));
        POKEMON_LIST.add(new Pokemon("Shiftry", 90, 100, 60, 90, 60, 80));
        POKEMON_LIST.add(new Pokemon("Swellow", 60, 85, 60, 75, 50, 125));
        POKEMON_LIST.add(new Pokemon("Pelipper", 60, 50, 100, 85, 70, 65));
        POKEMON_LIST.add(new Pokemon("Gardevoir", 68, 65, 65, 125, 115, 80));
        POKEMON_LIST.add(new Pokemon("Masquerain", 70, 60, 62, 80, 82, 60));
        POKEMON_LIST.add(new Pokemon("Breloom", 60, 130, 80, 60, 60, 70));
        POKEMON_LIST.add(new Pokemon("Slaking", 150, 160, 100, 95, 65, 100));
        POKEMON_LIST.add(new Pokemon("Ninjask", 61, 90, 45, 50, 50, 160));
        POKEMON_LIST.add(new Pokemon("Shedinja", 1, 90, 45, 30, 30, 40));
        POKEMON_LIST.add(new Pokemon("Exploud", 104, 91, 63, 91, 73, 68));
        POKEMON_LIST.add(new Pokemon("Hariyama", 144, 120, 60, 40, 60, 50));
        POKEMON_LIST.add(new Pokemon("Delcatty", 70, 65, 65, 55, 55, 70));
        POKEMON_LIST.add(new Pokemon("Sableye", 50, 75, 75, 65, 65, 50));
        POKEMON_LIST.add(new Pokemon("Mawile", 50, 85, 85, 55, 55, 50));
        POKEMON_LIST.add(new Pokemon("Aggron", 70, 110, 180, 60, 60, 50));
        POKEMON_LIST.add(new Pokemon("Manectric", 70, 75, 60, 105, 60, 105));
        POKEMON_LIST.add(new Pokemon("Plusle", 60, 50, 40, 85, 75, 95));
        POKEMON_LIST.add(new Pokemon("Minun", 60, 40, 50, 75, 85, 95));
        POKEMON_LIST.add(new Pokemon("Volbeat", 65, 73, 75, 47, 85, 85));
        POKEMON_LIST.add(new Pokemon("Illumise", 65, 47, 75, 73, 85, 85));
        POKEMON_LIST.add(new Pokemon("Roselia", 50, 60, 45, 100, 80, 65));
        POKEMON_LIST.add(new Pokemon("Swalot", 100, 73, 83, 73, 83, 55));
        POKEMON_LIST.add(new Pokemon("Sharpedo", 70, 120, 40, 95, 40, 95));
        POKEMON_LIST.add(new Pokemon("Wailord", 170, 90, 45, 90, 45, 60));
        POKEMON_LIST.add(new Pokemon("Camerupt", 70, 100, 70, 105, 75, 40));
        POKEMON_LIST.add(new Pokemon("Torkoal", 70, 85, 140, 85, 70, 20));
        POKEMON_LIST.add(new Pokemon("Grumpig", 80, 45, 65, 90, 110, 80));
        POKEMON_LIST.add(new Pokemon("Spinda", 60, 60, 60, 60, 60, 60));
        POKEMON_LIST.add(new Pokemon("Flygon", 80, 100, 80, 80, 80, 100));
        POKEMON_LIST.add(new Pokemon("Cacturne", 70, 115, 60, 115, 60, 55));
        POKEMON_LIST.add(new Pokemon("Altaria", 75, 70, 90, 70, 105, 80));
        POKEMON_LIST.add(new Pokemon("Zangoose", 73, 115, 60, 60, 60, 90));
        POKEMON_LIST.add(new Pokemon("Seviper", 73, 100, 60, 100, 60, 65));
        POKEMON_LIST.add(new Pokemon("Lunatone", 90, 55, 65, 95, 85, 70));
        POKEMON_LIST.add(new Pokemon("Solrock", 90, 95, 85, 55, 65, 70));
        POKEMON_LIST.add(new Pokemon("Whiscash", 110, 78, 73, 76, 71, 60));
        POKEMON_LIST.add(new Pokemon("Crawdaunt", 63, 120, 85, 90, 55, 55));
        POKEMON_LIST.add(new Pokemon("Claydol", 60, 70, 105, 70, 120, 75));
        POKEMON_LIST.add(new Pokemon("Cradily", 86, 81, 97, 81, 107, 43));
        POKEMON_LIST.add(new Pokemon("Armaldo", 75, 125, 100, 70, 80, 45));
        POKEMON_LIST.add(new Pokemon("Milotic", 95, 60, 79, 100, 125, 81));
        POKEMON_LIST.add(new Pokemon("Castform", 70, 70, 70, 70, 70, 70));
        POKEMON_LIST.add(new Pokemon("Kecleon", 60, 90, 70, 60, 120, 40));
        POKEMON_LIST.add(new Pokemon("Banette", 64, 115, 65, 83, 63, 65));
        POKEMON_LIST.add(new Pokemon("Dusclops", 40, 70, 130, 60, 130, 25));
        POKEMON_LIST.add(new Pokemon("Tropius", 99, 68, 83, 72, 87, 51));
        POKEMON_LIST.add(new Pokemon("Chimecho", 75, 50, 80, 95, 90, 65));
        POKEMON_LIST.add(new Pokemon("Absol", 65, 130, 60, 75, 60, 75));
        POKEMON_LIST.add(new Pokemon("Glalie", 80, 80, 80, 80, 80, 80));
        POKEMON_LIST.add(new Pokemon("Walrein", 110, 80, 90, 95, 90, 65));
        POKEMON_LIST.add(new Pokemon("Clamperl", 35, 64, 85, 74, 55, 32));
        POKEMON_LIST.add(new Pokemon("Huntail", 55, 104, 105, 94, 75, 52));
        POKEMON_LIST.add(new Pokemon("Gorebyss", 55, 84, 105, 114, 75, 52));
        POKEMON_LIST.add(new Pokemon("Relicanth", 100, 90, 130, 45, 65, 55));
        POKEMON_LIST.add(new Pokemon("Luvdisc", 43, 30, 55, 40, 65, 97));
        POKEMON_LIST.add(new Pokemon("Salamence", 95, 135, 80, 110, 80, 100));
        POKEMON_LIST.add(new Pokemon("Metagross", 80, 135, 130, 95, 90, 70));
        POKEMON_LIST.add(new Pokemon("Regirock", 80, 100, 200, 50, 100, 50));
        POKEMON_LIST.add(new Pokemon("Regice", 80, 50, 100, 100, 200, 50));
        POKEMON_LIST.add(new Pokemon("Registeel", 80, 75, 150, 75, 150, 50));
        POKEMON_LIST.add(new Pokemon("Latias", 80, 80, 90, 110, 130, 110));
        POKEMON_LIST.add(new Pokemon("Latios", 80, 90, 80, 130, 110, 110));
        POKEMON_LIST.add(new Pokemon("Kyogre", 100, 100, 90, 150, 140, 90));
        POKEMON_LIST.add(new Pokemon("Groudon", 100, 150, 140, 100, 90, 90));
        POKEMON_LIST.add(new Pokemon("Rayquaza", 105, 150, 90, 150, 90, 95));
        POKEMON_LIST.add(new Pokemon("Jirachi", 100, 100, 100, 100, 100, 100));
        POKEMON_LIST.add(new Pokemon("Deoxys", 50, 150, 50, 150, 50, 150));



        POKEMON_LIST.add(new Pokemon("Torterra", 95, 109, 105, 75, 85, 56));
        POKEMON_LIST.add(new Pokemon("Infernape", 76, 104, 71, 104, 71, 108));
        POKEMON_LIST.add(new Pokemon("Empoleon", 84, 86, 88, 111, 101, 60));
        POKEMON_LIST.add(new Pokemon("Luxray", 80, 120, 79, 95, 79, 70));
        POKEMON_LIST.add(new Pokemon("Staraptor", 85, 120, 70, 50, 60, 100));
        POKEMON_LIST.add(new Pokemon("Roserade", 60, 70, 65, 125, 105, 90));
        POKEMON_LIST.add(new Pokemon("Rampardos", 97, 165, 60, 65, 50, 58));
        POKEMON_LIST.add(new Pokemon("Bastiodon", 60, 52, 168, 47, 138, 30));
        POKEMON_LIST.add(new Pokemon("Garchomp", 108, 130, 95, 80, 85, 102));
        POKEMON_LIST.add(new Pokemon("Lucario", 70, 110, 70, 115, 70, 90));
        POKEMON_LIST.add(new Pokemon("Hippowdon", 108, 112, 118, 68, 72, 47));
        POKEMON_LIST.add(new Pokemon("Drapion", 70, 90, 110, 60, 75, 95));
        POKEMON_LIST.add(new Pokemon("Toxicroak", 83, 106, 65, 86, 65, 85));
        POKEMON_LIST.add(new Pokemon("Abomasnow", 90, 92, 75, 92, 85, 60));
        POKEMON_LIST.add(new Pokemon("Weavile", 70, 120, 65, 45, 85, 125));
        POKEMON_LIST.add(new Pokemon("Magnezone", 70, 70, 115, 130, 90, 60));
        POKEMON_LIST.add(new Pokemon("Lickilicky", 110, 85, 95, 80, 95, 50));
        POKEMON_LIST.add(new Pokemon("Rhyperior", 115, 140, 130, 55, 55, 40));
        POKEMON_LIST.add(new Pokemon("Tangrowth", 100, 100, 125, 110, 50, 50));
        POKEMON_LIST.add(new Pokemon("Electivire", 75, 123, 67, 95, 85, 95));
        POKEMON_LIST.add(new Pokemon("Magmortar", 75, 95, 67, 125, 95, 83));
        POKEMON_LIST.add(new Pokemon("Togekiss", 85, 50, 95, 120, 115, 80));
        POKEMON_LIST.add(new Pokemon("Yanmega", 86, 76, 86, 116, 56, 95));
        POKEMON_LIST.add(new Pokemon("Leafeon", 65, 110, 130, 60, 65, 95));
        POKEMON_LIST.add(new Pokemon("Glaceon", 65, 60, 110, 130, 95, 65));
        POKEMON_LIST.add(new Pokemon("Gliscor", 75, 95, 125, 45, 75, 95));
        POKEMON_LIST.add(new Pokemon("Mamoswine", 110, 130, 80, 70, 60, 80));
        POKEMON_LIST.add(new Pokemon("Porygon-Z", 85, 80, 70, 135, 75, 90));
        POKEMON_LIST.add(new Pokemon("Gallade", 68, 125, 65, 65, 115, 80));
        POKEMON_LIST.add(new Pokemon("Probopass", 60, 55, 145, 75, 150, 40));
        POKEMON_LIST.add(new Pokemon("Dusknoir", 45, 100, 135, 65, 135, 45));
        POKEMON_LIST.add(new Pokemon("Froslass", 70, 80, 70, 80, 70, 110));
        POKEMON_LIST.add(new Pokemon("Rotom", 50, 50, 77, 95, 77, 91));
        POKEMON_LIST.add(new Pokemon("Uxie", 75, 75, 130, 75, 130, 95));
        POKEMON_LIST.add(new Pokemon("Mesprit", 80, 105, 105, 105, 105, 80));
        POKEMON_LIST.add(new Pokemon("Azelf", 75, 125, 70, 125, 70, 115));
        POKEMON_LIST.add(new Pokemon("Dialga", 100, 120, 120, 150, 100, 90));
        POKEMON_LIST.add(new Pokemon("Palkia", 90, 120, 100, 150, 120, 100));
        POKEMON_LIST.add(new Pokemon("Heatran", 91, 90, 106, 130, 106, 77));
        POKEMON_LIST.add(new Pokemon("Regigigas", 110, 160, 110, 80, 110, 100));
        POKEMON_LIST.add(new Pokemon("Giratina", 150, 120, 100, 120, 100, 90));
        POKEMON_LIST.add(new Pokemon("Cresselia", 120, 70, 120, 75, 130, 85));
        POKEMON_LIST.add(new Pokemon("Phione", 80, 80, 80, 80, 80, 80));
        POKEMON_LIST.add(new Pokemon("Manaphy", 100, 100, 100, 100, 100, 100));
        POKEMON_LIST.add(new Pokemon("Darkrai", 70, 90, 90, 135, 90, 125));
        POKEMON_LIST.add(new Pokemon("Shaymin", 100, 100, 100, 100, 100, 100));
        POKEMON_LIST.add(new Pokemon("Arceus", 120, 120, 120, 120, 120, 120));

        POKEMON_LIST.add(new Pokemon("Serperior", 75, 75, 95, 75, 95, 113));
        POKEMON_LIST.add(new Pokemon("Emboar", 110, 123, 65, 100, 65, 65));
        POKEMON_LIST.add(new Pokemon("Samurott", 95, 100, 85, 108, 70, 70));
        POKEMON_LIST.add(new Pokemon("Watchog", 60, 85, 69, 60, 69, 77));
        POKEMON_LIST.add(new Pokemon("Stoutland", 85, 110, 90, 45, 90, 80));
        POKEMON_LIST.add(new Pokemon("Liepard", 64, 88, 50, 88, 50, 106));
        POKEMON_LIST.add(new Pokemon("Simisage", 75, 98, 63, 98, 63, 101));
        POKEMON_LIST.add(new Pokemon("Simisear", 75, 98, 63, 98, 63, 101));
        POKEMON_LIST.add(new Pokemon("Simipour", 75, 98, 63, 98, 63, 101));
        POKEMON_LIST.add(new Pokemon("Musharna", 116, 55, 85, 107, 95, 29));
        POKEMON_LIST.add(new Pokemon("Unfezant", 80, 115, 80, 65, 55, 93));
        POKEMON_LIST.add(new Pokemon("Zebstrika", 75, 100, 63, 80, 63, 116));
        POKEMON_LIST.add(new Pokemon("Gigalith", 85, 135, 130, 60, 80, 25));
        POKEMON_LIST.add(new Pokemon("Swoobat", 67, 57, 55, 77, 55, 114));
        POKEMON_LIST.add(new Pokemon("Excadrill", 110, 135, 60, 50, 65, 88));
        POKEMON_LIST.add(new Pokemon("Audino", 103, 60, 86, 60, 86, 50));
        POKEMON_LIST.add(new Pokemon("Conkeldurr", 105, 140, 95, 55, 65, 45));
        POKEMON_LIST.add(new Pokemon("Seismitoad", 105, 95, 75, 85, 75, 74));
        POKEMON_LIST.add(new Pokemon("Throh", 120, 100, 85, 30, 85, 45));
        POKEMON_LIST.add(new Pokemon("Sawk", 75, 125, 75, 30, 75, 85));
        POKEMON_LIST.add(new Pokemon("Leavanny", 75, 103, 80, 70, 80, 92));
        POKEMON_LIST.add(new Pokemon("Scolipede", 60, 100, 89, 55, 69, 112));
        POKEMON_LIST.add(new Pokemon("Whimsicott", 60, 67, 85, 77, 75, 116));
        POKEMON_LIST.add(new Pokemon("Lilligant", 70, 60, 75, 110, 75, 90));
        POKEMON_LIST.add(new Pokemon("Krookodile", 95, 117, 80, 65, 70, 92));
        POKEMON_LIST.add(new Pokemon("Darmanitan", 105, 140, 55, 30, 55, 95));
        POKEMON_LIST.add(new Pokemon("Maractus", 75, 86, 67, 106, 67, 60));
        POKEMON_LIST.add(new Pokemon("Crustle", 70, 95, 125, 65, 75, 45));
        POKEMON_LIST.add(new Pokemon("Scrafty", 65, 90, 115, 45, 115, 58));
        POKEMON_LIST.add(new Pokemon("Sigilyph", 72, 58, 80, 103, 80, 97));
        POKEMON_LIST.add(new Pokemon("Cofagrigus", 58, 50, 145, 95, 105, 30));
        POKEMON_LIST.add(new Pokemon("Carracosta", 74, 108, 133, 83, 65, 32));
        POKEMON_LIST.add(new Pokemon("Archeops", 75, 140, 65, 112, 65, 110));
        POKEMON_LIST.add(new Pokemon("Garbodor", 80, 95, 82, 60, 82, 75));
        POKEMON_LIST.add(new Pokemon("Zoroark", 60, 105, 60, 120, 60, 105));
        POKEMON_LIST.add(new Pokemon("Cinccino", 75, 95, 60, 65, 60, 115));
        POKEMON_LIST.add(new Pokemon("Gothitelle", 70, 55, 95, 95, 110, 65));
        POKEMON_LIST.add(new Pokemon("Reuniclus", 110, 65, 75, 125, 85, 30));
        POKEMON_LIST.add(new Pokemon("Swanna", 75, 87, 63, 87, 63, 98));
        POKEMON_LIST.add(new Pokemon("Vanilluxe", 71, 95, 85, 110, 95, 79));
        POKEMON_LIST.add(new Pokemon("Sawsbuck", 80, 100, 70, 60, 70, 95));
        POKEMON_LIST.add(new Pokemon("Emolga", 55, 75, 60, 75, 60, 103));
        POKEMON_LIST.add(new Pokemon("Escavalier", 70, 135, 105, 60, 105, 20));
        POKEMON_LIST.add(new Pokemon("Amoonguss", 114, 85, 70, 85, 80, 30));
        POKEMON_LIST.add(new Pokemon("Jellicent", 100, 60, 70, 85, 105, 60));
        POKEMON_LIST.add(new Pokemon("Alomomola", 165, 75, 80, 40, 45, 65));
        POKEMON_LIST.add(new Pokemon("Galvantula", 70, 77, 60, 97, 60, 108));
        POKEMON_LIST.add(new Pokemon("Ferrothorn", 74, 94, 131, 54, 116, 20));
        POKEMON_LIST.add(new Pokemon("Klinklang", 60, 100, 115, 70, 85, 90));
        POKEMON_LIST.add(new Pokemon("Eelektross", 85, 115, 80, 105, 80, 50));
        POKEMON_LIST.add(new Pokemon("Beheeyem", 75, 75, 75, 125, 95, 40));
        POKEMON_LIST.add(new Pokemon("Chandelure", 60, 55, 90, 145, 90, 80));
        POKEMON_LIST.add(new Pokemon("Haxorus", 76, 147, 90, 60, 70, 97));
        POKEMON_LIST.add(new Pokemon("Beartic", 95, 130, 80, 70, 80, 50));
        POKEMON_LIST.add(new Pokemon("Cryogonal", 80, 50, 50, 95, 135, 105));
        POKEMON_LIST.add(new Pokemon("Accelgor", 80, 70, 40, 100, 60, 145));
        POKEMON_LIST.add(new Pokemon("Stunfisk", 109, 66, 84, 81, 99, 32));
        POKEMON_LIST.add(new Pokemon("Mienshao", 65, 125, 60, 95, 60, 105));
        POKEMON_LIST.add(new Pokemon("Druddigon", 77, 120, 90, 60, 90, 48));
        POKEMON_LIST.add(new Pokemon("Golurk", 89, 124, 80, 55, 80, 55));
        POKEMON_LIST.add(new Pokemon("Bisharp", 65, 125, 100, 60, 70, 70));
        POKEMON_LIST.add(new Pokemon("Bouffalant", 95, 110, 95, 40, 95, 55));
        POKEMON_LIST.add(new Pokemon("Braviary", 100, 123, 75, 57, 75, 80));
        POKEMON_LIST.add(new Pokemon("Mandibuzz", 110, 65, 105, 55, 95, 80));
        POKEMON_LIST.add(new Pokemon("Heatmor", 85, 97, 66, 105, 66, 65));
        POKEMON_LIST.add(new Pokemon("Durant", 58, 109, 112, 48, 48, 109));
        POKEMON_LIST.add(new Pokemon("Hydreigon", 92, 105, 90, 125, 90, 98));
        POKEMON_LIST.add(new Pokemon("Volcarona", 85, 60, 65, 135, 105, 100));

// Legendary Pokémon from Unova
        POKEMON_LIST.add(new Pokemon("Cobalion", 91, 90, 129, 90, 72, 108));
        POKEMON_LIST.add(new Pokemon("Terrakion", 91, 129, 90, 72, 90, 108));
        POKEMON_LIST.add(new Pokemon("Virizion", 91, 90, 72, 90, 129, 108));
        POKEMON_LIST.add(new Pokemon("Tornadus", 79, 115, 70, 125, 80, 111));
        POKEMON_LIST.add(new Pokemon("Thundurus", 79, 115, 70, 125, 80, 111));
        POKEMON_LIST.add(new Pokemon("Reshiram", 100, 120, 100, 150, 120, 90));
        POKEMON_LIST.add(new Pokemon("Zekrom", 100, 150, 120, 120, 100, 90));
        POKEMON_LIST.add(new Pokemon("Landorus", 89, 125, 90, 115, 80, 101));
        POKEMON_LIST.add(new Pokemon("Kyurem", 125, 130, 90, 130, 90, 95));
        POKEMON_LIST.add(new Pokemon("Keldeo", 91, 72, 90, 129, 90, 108));
        POKEMON_LIST.add(new Pokemon("Meloetta", 100, 77, 77, 128, 128, 90));
        POKEMON_LIST.add(new Pokemon("Genesect", 71, 120, 95, 120, 95, 99));

        POKEMON_LIST.add(new Pokemon("Chesnaught", 88, 107, 122, 74, 75, 64));
        POKEMON_LIST.add(new Pokemon("Delphox", 75, 69, 72, 114, 100, 104));
        POKEMON_LIST.add(new Pokemon("Greninja", 72, 95, 67, 103, 71, 122));
        POKEMON_LIST.add(new Pokemon("Diggersby", 85, 56, 77, 50, 77, 78));
        POKEMON_LIST.add(new Pokemon("Talonflame", 78, 81, 71, 74, 69, 126));
        POKEMON_LIST.add(new Pokemon("Vivillon", 80, 52, 50, 90, 50, 89));
        POKEMON_LIST.add(new Pokemon("Pyroar", 86, 68, 72, 109, 66, 106));
        POKEMON_LIST.add(new Pokemon("Florges", 78, 65, 68, 112, 154, 75));
        POKEMON_LIST.add(new Pokemon("Gogoat", 123, 100, 62, 97, 81, 68));
        POKEMON_LIST.add(new Pokemon("Pangoro", 95, 124, 78, 69, 71, 58));
        POKEMON_LIST.add(new Pokemon("Furfrou", 75, 80, 60, 65, 90, 102));
        POKEMON_LIST.add(new Pokemon("Meowstic", 74, 48, 76, 83, 81, 104));
        POKEMON_LIST.add(new Pokemon("Aegislash", 60, 150, 50, 150, 50, 60));
        POKEMON_LIST.add(new Pokemon("Aromatisse", 101, 72, 72, 99, 89, 29));
        POKEMON_LIST.add(new Pokemon("Slurpuff", 82, 80, 86, 85, 75, 72));
        POKEMON_LIST.add(new Pokemon("Malamar", 86, 92, 88, 68, 75, 73));
        POKEMON_LIST.add(new Pokemon("Barbaracle", 72, 105, 115, 54, 86, 68));
        POKEMON_LIST.add(new Pokemon("Dragalge", 65, 75, 90, 97, 123, 44));
        POKEMON_LIST.add(new Pokemon("Clawitzer", 71, 73, 88, 120, 89, 59));
        POKEMON_LIST.add(new Pokemon("Heliolisk", 62, 55, 52, 109, 94, 109));
        POKEMON_LIST.add(new Pokemon("Tyrantrum", 82, 121, 119, 69, 59, 71));
        POKEMON_LIST.add(new Pokemon("Aurorus", 123, 77, 72, 99, 92, 58));
        POKEMON_LIST.add(new Pokemon("Sylveon", 95, 65, 65, 110, 130, 60));
        POKEMON_LIST.add(new Pokemon("Hawlucha", 78, 92, 75, 74, 63, 118));
        POKEMON_LIST.add(new Pokemon("Dedenne", 67, 58, 57, 81, 67, 101));
        POKEMON_LIST.add(new Pokemon("Carbink", 50, 50, 150, 50, 150, 50));
        POKEMON_LIST.add(new Pokemon("Goodra", 90, 100, 70, 110, 150, 80));
        POKEMON_LIST.add(new Pokemon("Klefki", 57, 80, 91, 80, 87, 75));
        POKEMON_LIST.add(new Pokemon("Trevenant", 85, 110, 76, 65, 82, 56));
        POKEMON_LIST.add(new Pokemon("Gourgeist", 65, 90, 122, 58, 75, 84)); // Average form
        POKEMON_LIST.add(new Pokemon("Avalugg", 95, 117, 184, 44, 46, 28));
        POKEMON_LIST.add(new Pokemon("Noivern", 85, 70, 80, 97, 80, 123));
        POKEMON_LIST.add(new Pokemon("Xerneas", 126, 131, 95, 131, 98, 99));
        POKEMON_LIST.add(new Pokemon("Yveltal", 126, 131, 95, 131, 98, 99));
        POKEMON_LIST.add(new Pokemon("Zygarde", 108, 100, 121, 81, 95, 95)); // 50% Forme
        POKEMON_LIST.add(new Pokemon("Diancie", 50, 100, 150, 100, 150, 50));
        POKEMON_LIST.add(new Pokemon("Hoopa", 80, 110, 60, 150, 130, 70)); // Confined Form
        POKEMON_LIST.add(new Pokemon("Volcanion", 80, 110, 120, 130, 90, 70));

        POKEMON_LIST.add(new Pokemon("Nihilego", 109, 53, 47, 71, 131, 103));
        POKEMON_LIST.add(new Pokemon("Buzzwole", 107, 139, 139, 53, 53, 79));
        POKEMON_LIST.add(new Pokemon("Pheromosa", 71, 137, 37, 137, 37, 151));
        POKEMON_LIST.add(new Pokemon("Xurkitree", 83, 89, 71, 173, 71, 83));
        POKEMON_LIST.add(new Pokemon("Celesteela", 97, 101, 103, 107, 101, 76));
        POKEMON_LIST.add(new Pokemon("Kartana", 59, 181, 131, 59, 31, 109));
        POKEMON_LIST.add(new Pokemon("Guzzlord", 223, 101, 53, 97, 53, 43));
        POKEMON_LIST.add(new Pokemon("Solgaleo", 137, 137, 107, 113, 89, 97));
        POKEMON_LIST.add(new Pokemon("Lunala", 137, 113, 89, 137, 107, 97));
        POKEMON_LIST.add(new Pokemon("Necrozma", 97, 107, 101, 127, 89, 79));
        POKEMON_LIST.add(new Pokemon("Magearna", 80, 95, 115, 130, 115, 65));
        POKEMON_LIST.add(new Pokemon("Marshadow", 90, 125, 80, 90, 90, 125));
        POKEMON_LIST.add(new Pokemon("Zeraora", 88, 112, 75, 102, 80, 143));
        POKEMON_LIST.add(new Pokemon("Cufant", 72, 80, 95, 55, 55, 65));
        POKEMON_LIST.add(new Pokemon("Copperajah", 122, 130, 69, 80, 69, 30));
        POKEMON_LIST.add(new Pokemon("Frosmoth", 70, 65, 60, 125, 90, 65));
        POKEMON_LIST.add(new Pokemon("Blacephalon", 53, 127, 53, 151, 79, 107));

    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase("c!random") && args.length > 1) {
            if (isAllowedChannel(event.getChannel().getId())) {
                if (challengeActive) {
                    event.getChannel().sendMessage("A challenge is already in progress. Please wait for it to finish.").queue();
                    return;
                }

                challengedUserId = args[1].replaceAll("[^0-9]", "");
                challengerId = event.getAuthor().getId();
                challengeUser(event);
            } else {
                sendAllowedChannelsMessage(event);
            }
        }

        if (args[0].equalsIgnoreCase("c!startr")) {
            confirmChallenge(event);
        }

        if (args[0].equalsIgnoreCase("c!pick")) {
            if (!challengeActive || !challengeConfirmed) {
                event.getChannel().sendMessage("The challenge has not been confirmed by a middleman yet.").queue();
                return;
            }

            pickPokemon(event);
        }

        if (args[0].equalsIgnoreCase("c!stop")) {
            stopChallenge(event);
        }
    }

    private void challengeUser(MessageReceivedEvent event) {
        if (!isAllowedChannel(event.getChannel().getId())) {
            sendAllowedChannelsMessage(event);
            return;
        }

        if (challengeActive) {
            event.getChannel().sendMessage("A challenge is already in progress. Please wait for it to finish.").queue();
            return;
        }

        challengedUserId = event.getMessage().getContentRaw().split("\\s+")[1].replaceAll("[^0-9]", "");
        challengerId = event.getAuthor().getId();

        challengeActive = true;
        challengeConfirmed = false;

        event.getChannel().sendMessage("<@" + challengedUserId + ">, you have been challenged by <@" + challengerId + ">! A middleman must confirm the game with `c!startr`.").queue();
    }

    private void confirmChallenge(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        if (!MIDDLEMAN_IDS.contains(userId)) {
            event.getChannel().sendMessage("Only a middleman can confirm the game.").queue();
            return;
        }

        if (!challengeActive) {
            event.getChannel().sendMessage("No challenge is currently active.").queue();
            return;
        }

        if (challengeConfirmed) {
            event.getChannel().sendMessage("The challenge has already been confirmed.").queue();
            return;
        }

        middlemanId = userId;
        challengeConfirmed = true;
        event.getChannel().sendMessage("Challenge confirmed by <@" + middlemanId + ">! Both players can now pick their Pokémon using `c!pick`.").queue();
    }

    private void pickPokemon(MessageReceivedEvent event) {
        String userId = event.getAuthor().getId();
        if (!userId.equals(challengerId) && !userId.equals(challengedUserId)) {
            event.getChannel().sendMessage("You are not part of the current challenge.").queue();
            return;
        }

        if (playerPicks.containsKey(userId)) {
            event.getChannel().sendMessage("You have already picked a Pokémon.").queue();
            return;
        }

        Pokemon randomPokemon = getRandomPokemon();
        playerPicks.put(userId, randomPokemon);

        String gifUrl = getPokemonGif(randomPokemon.name);
        event.getChannel().sendMessage(gifUrl).queue();
        event.getChannel().sendMessage("You picked " + randomPokemon.name + " with base stats:\n" + randomPokemon + "\nTotal base power: " + randomPokemon.totalBasePower()).queue();

        if (playerPicks.size() == 2) {
            announceWinner(event);
        }
    }

    private void stopChallenge(MessageReceivedEvent event) {
        if (!challengeActive) {
            event.getChannel().sendMessage("No challenge is currently active.").queue();
            return;
        }

        event.getChannel().sendMessage("The challenge has been stopped.").queue();
        resetChallenge();
    }

    private void resetChallenge() {
        playerPicks.clear();
        challengerId = null;
        challengedUserId = null;
        middlemanId = null;
        challengeActive = false;
        challengeConfirmed = false;
    }

    private boolean isAllowedChannel(String channelId) {
        return ALLOWED_CHANNEL_IDS.contains(channelId);
    }

    private void sendAllowedChannelsMessage(MessageReceivedEvent event) {
        List<String> channelMentions = new ArrayList<>();
        for (String channelId : ALLOWED_CHANNEL_IDS) {
            channelMentions.add("<#" + channelId + ">");
        }
        event.getChannel().sendMessage("Please use one of the allowed channels for this command: " + String.join(" or ", channelMentions)).queue();
    }

    private Pokemon getRandomPokemon() {
        Random random = new Random();
        return POKEMON_LIST.get(random.nextInt(POKEMON_LIST.size()));
    }

    private String getPokemonGif(String pokemonName) {
        return "https://play.pokemonshowdown.com/sprites/xyani-shiny/" + pokemonName.toLowerCase() + ".gif";
    }

    private void announceWinner(MessageReceivedEvent event) {
        Pokemon challengerPokemon = playerPicks.get(challengerId);
        Pokemon challengedPokemon = playerPicks.get(challengedUserId);

        int challengerPower = challengerPokemon.totalBasePower();
        int challengedPower = challengedPokemon.totalBasePower();

        StringBuilder resultMessage = new StringBuilder();
        resultMessage.append("Challenger's Pokémon: ").append(challengerPokemon.name)
                .append(" with total base power: ").append(challengerPower).append("\n")
                .append("Challenged User's Pokémon: ").append(challengedPokemon.name)
                .append(" with total base power: ").append(challengedPower).append("\n");

        if (challengerPower > challengedPower) {
            resultMessage.append("<@").append(challengerId).append("> wins with ").append(challengerPokemon.name)
                    .append("! Share your winnings in the winnings channel!");
        } else if (challengedPower > challengerPower) {
            resultMessage.append("<@").append(challengedUserId).append("> wins with ").append(challengedPokemon.name)
                    .append("! Share your winnings in the winnings channel!");
        } else {
            resultMessage.append("It's a tie! Both Pokémon have equal power.");
        }

        event.getChannel().sendMessage(resultMessage.toString()).queue();
        resetChallenge();
    }


    private static class Pokemon {
        private final String name;
        private final int hp, attack, defense, spAttack, spDefense, speed;

        public Pokemon(String name, int hp, int attack, int defense, int spAttack, int spDefense, int speed) {
            this.name = name;
            this.hp = hp;
            this.attack = attack;
            this.defense = defense;
            this.spAttack = spAttack;
            this.spDefense = spDefense;
            this.speed = speed;
        }

        public int totalBasePower() {
            return hp + attack + defense + spAttack + spDefense + speed;
        }

        @Override
        public String toString() {
            return "```" +
                    "HP: " + hp + "\n" +
                    "Attack: " + attack + "\n" +
                    "Defense: " + defense + "\n" +
                    "Special Attack: " + spAttack + "\n" +
                    "Special Defense: " + spDefense + "\n" +
                    "Speed: " + speed +
                    "```";
        }
    }
}