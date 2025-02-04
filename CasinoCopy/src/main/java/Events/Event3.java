package Events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;

public class Event3 extends ListenerAdapter {

    private static final List<String> ALLOWED_CHANNEL_IDS = Arrays.asList("1323664289755824260", "1323664415618236416"); // Replace with your actual channel IDs
    private static final Set<String> MIDDLEMAN_IDS = new HashSet<>(Arrays.asList("700580614029574235", "767386032370089995","360815908274438148")); // Middleman's Discord IDs

    private static class GameState {
        long currentMax;
        User player1;
        User player2;
        boolean player1Turn;
        boolean isStarted;

        GameState(long currentMax, User player1, User player2) {
            this.currentMax = currentMax;
            this.player1 = player1;
            this.player2 = player2;
            this.player1Turn = true;
            this.isStarted = false;
        }
    }

    private final Map<String, GameState> activeGames = new HashMap<>();
    private final Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length > 0 && (args[0].equalsIgnoreCase("c!deathroll") || args[0].equalsIgnoreCase("c!roll") || args[0].equalsIgnoreCase("c!stopdeathroll") || args[0].equalsIgnoreCase("c!startdr"))) {
            if (!isAllowedChannel(event.getChannel().getId())) {
                event.getChannel().sendMessage("Please use one of the allowed channels for this command: <#1249272470381527050> or <#1249693294229852191>").queue();
                return;
            }
        }

        if (args[0].equalsIgnoreCase("c!deathroll")) {
            if (args.length < 3) {
                event.getChannel().sendMessage("Usage: c!deathroll <starting_number> <@opponent>").queue();
                return;
            }

            long startingNumber;
            try {
                startingNumber = Long.parseLong(args[1]);
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Please provide a valid starting number.").queue();
                return;
            }

            User player1 = event.getAuthor();
            if (event.getMessage().getMentions().getUsers().isEmpty()) {
                event.getChannel().sendMessage("Please mention a valid opponent.").queue();
                return;
            }
            User player2 = event.getMessage().getMentions().getUsers().get(0);

            if (activeGames.containsKey(player1.getId()) || activeGames.containsKey(player2.getId())) {
                event.getChannel().sendMessage("One of the players is already in an active game.").queue();
                return;
            }

            GameState gameState = new GameState(startingNumber, player1, player2);
            activeGames.put(player1.getId(), gameState);
            activeGames.put(player2.getId(), gameState);

            event.getChannel().sendMessage("Deathroll game started between " + player1.getName() + " and " + player2.getName() + " with starting number " + startingNumber + ". " + player1.getName() + " goes first! Waiting for middleman to start the game.").queue();
        } else if (args[0].equalsIgnoreCase("c!startdr")) {
            User middleman = event.getAuthor();
            if (!MIDDLEMAN_IDS.contains(middleman.getId())) {
                event.getChannel().sendMessage("Only the middleman can start the game.").queue();
                return;
            }

            GameState gameState = getActiveGame();
            if (gameState == null) {
                event.getChannel().sendMessage("No active game to start. Make sure the players have created a Deathroll game first.").queue();
                return;
            }

            if (gameState.isStarted) {
                event.getChannel().sendMessage("The game has already started.").queue();
                return;
            }

            // Middleman can now start the game and handle the money collection
            gameState.isStarted = true;
            event.getChannel().sendMessage("The game between " + gameState.player1.getName() + " and " + gameState.player2.getName() + " has started!").queue();
        } else if (args[0].equalsIgnoreCase("c!roll")) {
            User player = event.getAuthor();
            GameState gameState = activeGames.get(player.getId());

            if (gameState == null) {
                event.getChannel().sendMessage("You are not in an active Deathroll game.").queue();
                return;
            }

            if (!gameState.isStarted) {
                event.getChannel().sendMessage("The game hasn't started yet. Please wait for the middleman to start the game.").queue();
                return;
            }

            if ((gameState.player1Turn && !player.equals(gameState.player1)) || (!gameState.player1Turn && !player.equals(gameState.player2))) {
                event.getChannel().sendMessage("It's not your turn.").queue();
                return;
            }

            long roll;
            if (gameState.currentMax == 2) {
                roll = random.nextBoolean() ? 2 : 1;
            } else {
                roll = random.nextLong(gameState.currentMax - 1) + 1;
            }

            event.getChannel().sendMessage(player.getName() + " rolled " + roll + " out of " + gameState.currentMax + ".").queue();

            if (roll == 1) {
                event.getChannel().sendMessage(player.getName() + " rolled a 1 and loses the game!").queue();
                endDeathrollGame(gameState);
            } else {
                gameState.currentMax = roll;
                gameState.player1Turn = !gameState.player1Turn;
            }
        } else if (args[0].equalsIgnoreCase("c!stopdeathroll")) {
            User player = event.getAuthor();
            GameState gameState = activeGames.get(player.getId());

            if (gameState == null) {
                event.getChannel().sendMessage("You are not in an active Deathroll game.").queue();
                return;
            }

            endDeathrollGame(gameState);
            event.getChannel().sendMessage("The Deathroll game between " + gameState.player1.getName() + " and " + gameState.player2.getName() + " has been stopped.").queue();
        }
    }

    private boolean isAllowedChannel(String channelId) {
        return ALLOWED_CHANNEL_IDS.contains(channelId);
    }

    private GameState getActiveGame() {
        // Retrieve the first active game in the map
        return activeGames.values().stream().filter(game -> !game.isStarted).findFirst().orElse(null);
    }

    private void endDeathrollGame(GameState gameState) {
        activeGames.remove(gameState.player1.getId());
        activeGames.remove(gameState.player2.getId());
        User winner = gameState.player1Turn ? gameState.player2 : gameState.player1;
        winner.openPrivateChannel().queue(privateChannel -> privateChannel.sendMessage("Congratulations! Share your winnings in the winnings channel!").queue());
    }
}
