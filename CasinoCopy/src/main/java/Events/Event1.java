package Events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.utils.FileUpload;

import java.io.File;
import java.util.*;


public class Event1 extends ListenerAdapter {
    private static final List<String> ALLOWED_CHANNEL_IDS = Arrays.asList("1323664289755824260", "1323664415618236416");
    private Map<String, String[]> ongoingGame = new HashMap<>();
    private Map<String, Map<String, String>> playerCalls = new HashMap<>(); // Store calls with player IDs
    private List<String> middlemanIds = Arrays.asList("767386032370089995", "700580614029574235","360815908274438148");

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args[0].equalsIgnoreCase("c!flip")) {
            if (isAllowedChannel(event.getChannel().getId())) {
                startGame(event, args);
            } else {
                event.getChannel().sendMessage("Please use one of the allowed channels for this command: <#1323664289755824260> or <#1323664415618236416>").queue();
            }
        } else if (args[0].equalsIgnoreCase("?rules")) {
            respondWithRules(event);
        } else if (args[0].equalsIgnoreCase("c!start")) {
            startGameByMiddleman(event);
        } else if (args[0].equalsIgnoreCase("heads") || args[0].equalsIgnoreCase("tails")) {
            handlePlayerCall(event, args[0]);
        }
    }

    private boolean isAllowedChannel(String channelId) {
        return ALLOWED_CHANNEL_IDS.contains(channelId);
    }

    private void startGame(MessageReceivedEvent event, String[] args) {
        if (args.length < 2) {
            event.getChannel().sendMessage("Usage: `c!flip @opponent`.").queue();
            return;
        }
        
        String player1 = event.getAuthor().getId();
        String player2 = args[1].replaceAll("[<@!>]", ""); // Remove the @mention symbols to get the player ID

        if (player1.equals(player2)) {
            event.getChannel().sendMessage("You cannot play against yourself.").queue();
            return;
        }

        ongoingGame.put(event.getChannel().getId(), new String[]{player1, player2});
        playerCalls.remove(event.getChannel().getId()); // Clear previous player calls if any
        event.getChannel().sendMessage("A game has been initiated between <@" + player1 + "> and <@" + player2 + ">.\n"
                + "Both players must send their in-game money to the middleman. Once done, the middleman should type `c!start` to begin the game.\n"
                + "**Middleman PRO ign - Silver:- Majiko, Mrvermillion || Gold:- TheNikifreak, TheNikifreak9**").queue();}

    private void startGameByMiddleman(MessageReceivedEvent event) {
        String channelId = event.getChannel().getId();
        String userId = event.getAuthor().getId();

        if (!middlemanIds.contains(userId)) {
            event.getChannel().sendMessage("Only middlemen can start the game.").queue();
            return;
        }

        if (!ongoingGame.containsKey(channelId)) {
            event.getChannel().sendMessage("No ongoing game in this channel. Use `c!flip` to initiate a game.").queue();
            return;
        }

        String[] players = ongoingGame.get(channelId);
        String player1 = players[0];
        String player2 = players[1];

        event.getChannel().sendMessage("<@" + player1 + "> and <@" + player2 + ">, please call `heads` or `tails`.").queue();
        playerCalls.put(channelId, new HashMap<>()); // Initialize map for player calls
    }

    private void respondWithRules(MessageReceivedEvent event) {
        event.getChannel().sendMessage("*Rule #1:* **Exercise Caution**\n" +
                "- *Avoid gambling recklessly.* Only participate if you're financially prepared. Prudence is key.\n" +
                "\n" +
                "*Rule #2:* **Fulfill Your Commitments**\n" +
                "- *Always ensure you can meet your wagers.* Have the resources available to honor your bets.\n" +
                "\n" +
                "*Rule #3:* **Pokémon Gambling Protocol**\n" +
                "- *Interested in wagering Pokémon?*\n" +
                "  - *Contact <@360815908274438148>, <@767386032370089995> or <@700580614029574235> to establish a dedicated channel showcasing your Pokémon for gambling.*\n" +
                "  - *Pricing discussions occur in the lobby chat.*\n" +
                "  - **Limitation:** *Each channel can display up to 10 Pokémon. Choose your offerings carefully.*\n" +
                "  - *Channel Adjustments:* Inform a moderator for any channel modifications.\n" +
                "\n" +
                "*Rule #4:* **Respect Moderators**\n" +
                "- *Avoid harassing moderators.*\n" +
                "  - *Mods contribute to the community's enjoyment. Abuse of this rule will result in consequences, potentially including bans.*\n" +
                "\n" +
                "*Rule #5:* **No RMT (Real Money Trading)**\n" +
                "- *Any form of Real Money Trading (RMT) or Real World Trading (RWT) is strictly forbidden.*\n" +
                "  - *If any topics related to RMT/RWT are discussed, you will be reported to PRO staff and permanently banned from this server.*\n" +
                "\n" +
                "*Note:* **Please DM any of our admins to gain access to the rest of the channel.**").queue();

    }

    private void handlePlayerCall(MessageReceivedEvent event, String call) {
        String channelId = event.getChannel().getId();

        if (!ongoingGame.containsKey(channelId)) {
            return; // No ongoing game
        }

        String playerId = event.getAuthor().getId();
        String[] players = ongoingGame.get(channelId);

        // Check if the player is part of the game
        if (!(playerId.equals(players[0]) || playerId.equals(players[1]))) {
            event.getChannel().sendMessage("You are not part of the ongoing game.").queue();
            return;
        }

        String opponent = playerId.equals(players[0]) ? players[1] : players[0];
        Map<String, String> calls = playerCalls.get(channelId);

        if (calls.containsKey(playerId)) {
            event.getChannel().sendMessage("You have already made your call!").queue();
            return;
        }

        // Store the player's call
        calls.put(playerId, call);
        event.getChannel().sendMessage(event.getAuthor().getAsMention() + " has called " + call + "!\nWaiting for the other player...").queue();

        // Check if both players have made their calls
        if (calls.size() == 2) {
            // Check if both players have made the same call
            String player1Call = calls.get(players[0]);
            String player2Call = calls.get(players[1]);

            if (player1Call.equals(player2Call)) {
                event.getChannel().sendMessage("Both players cannot call the same result! Please make different calls.").queue();
                // Clear calls and prompt players to call again
                playerCalls.put(channelId, new HashMap<>());
                event.getChannel().sendMessage("<@" + players[0] + "> and <@" + players[1] + ">, please call `heads` or `tails` again.").queue();
            } else {
                // Both players have made different calls, proceed with the game
                startCountdown(event);
            }
        }
    }



    private void startCountdown(MessageReceivedEvent event) {
        String channelId = event.getChannel().getId();
        String[] players = ongoingGame.get(channelId);
        Map<String, String> calls = playerCalls.get(channelId);

        String player1Id = players[0];
        String player2Id = players[1];

        String player1Call = calls.get(player1Id);
        String player2Call = calls.get(player2Id);

        Random rand = new Random();
        boolean isHeads = rand.nextBoolean(); // Coin flip (true = heads, false = tails)

        // Determine the winner
        String winner = isHeads ? (player1Call.equals("heads") ? player1Id : player2Id) : (player1Call.equals("heads") ? player2Id : player1Id);

        event.getChannel().sendMessage("Flipping the coin...").queue(message -> {
            try {
                Thread.sleep(1000);
                message.editMessage("3").queue();
                Thread.sleep(1000);
                message.editMessage("2").queue();
                Thread.sleep(1000);
                message.editMessage("1").queue();
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            File file = new File(isHeads ? "gif/heads.gif" : "gif/tails.gif");
            event.getChannel().sendMessage("The result is:").addFiles(FileUpload.fromData(file)).queue();

            // Announce the winner
            event.getChannel().sendMessage("The winner is: <@" + winner + ">").queue();
            event.getChannel().sendMessage("Share your winnings in the winnings channel!").queue();

            // Clear the game data after completion
            ongoingGame.remove(channelId);
            playerCalls.remove(channelId);
        });
    }
}
