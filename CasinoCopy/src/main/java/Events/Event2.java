package Events;

import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Event2 extends ListenerAdapter {

    private static final List<String> ALLOWED_CHANNEL_IDS = Arrays.asList("1323664415618236416", "1323664289755824260");
    private static final List<String> MIDDLEMAN_IDS = Arrays.asList("700580614029574235", "767386032370089995","360815908274438148");
    private boolean jackpotActive = false;
    private boolean middlemanAllowed = false;
    private int requiredUsersCount = 0;
    private List<String> participants = new ArrayList<>();
    private String jackpotCommandUser;

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length > 0 && (args[0].equalsIgnoreCase("c!jackpot") || args[0].equalsIgnoreCase("me") || args[0].equalsIgnoreCase("c!stopjackpot") || args[0].equalsIgnoreCase("c!startj"))) {
            if (!isAllowedChannel(event.getChannel().getId())) {
                if (args[0].equalsIgnoreCase("me")) {
                    return; // Don't prompt anything for "me" command in a different channel
                }
                event.getChannel().sendMessage("Please use one of the allowed channels for this command: <#1249272470381527050> or <#1249693294229852191>").queue();
                return;
            }
        }

        switch (args[0].toLowerCase()) {
            case "c!jackpot":
                if (args.length == 2) startJackpot(event, args[1]);
                break;
            case "me":
                if (jackpotActive) handleMeCommand(event);
                break;
            case "c!stopjackpot":
                stopJackpot(event);
                break;
            case "c!startj":
                handleStartJCommand(event);
                break;
            default:
                break;
        }
    }

    private boolean isAllowedChannel(String channelId) {
        return ALLOWED_CHANNEL_IDS.contains(channelId);
    }

    private boolean isMiddleman(String userId) {
        return MIDDLEMAN_IDS.contains(userId);
    }

    private void startJackpot(MessageReceivedEvent event, String countStr) {
        if (jackpotActive) {
            event.getChannel().sendMessage("A jackpot is already active. Please wait until it finishes or is stopped.").queue();
            return;
        }
        try {
            requiredUsersCount = Integer.parseInt(countStr);
            if (requiredUsersCount > 1) {
                jackpotActive = true;
                jackpotCommandUser = event.getAuthor().getName();
                participants.clear();
                event.getChannel().sendMessage("Jackpot started by " + jackpotCommandUser + ". Type 'me' to join! We need " + requiredUsersCount + " participants to proceed.").queue();
            } else {
                event.getChannel().sendMessage("Number of users must be more than 1 to start the jackpot.").queue();
            }
        } catch (NumberFormatException e) {
            event.getChannel().sendMessage("Please provide a valid number for participants count.").queue();
        }
    }

    private void handleMeCommand(MessageReceivedEvent event) {
        String userName = event.getAuthor().getName();
        if (!participants.contains(userName)) {
            participants.add(userName);
            event.getChannel().sendMessage(userName + " has entered the jackpot! (" + participants.size() + "/" + requiredUsersCount + ")")
                    .queue(message -> message.addReaction(Emoji.fromUnicode("U+2705")).queue()); // Checkmark emoji

            if (participants.size() >= requiredUsersCount) {
                middlemanAllowed = true;
                event.getChannel().sendMessage("Participant limit reached! A middleman can now use `c!startj` to proceed.").queue();
            }
        } else {
            event.getChannel().sendMessage(userName + ", you have already entered the jackpot.").queue();
        }
    }

    private void handleStartJCommand(MessageReceivedEvent event) {
        if (!middlemanAllowed) {
            event.getChannel().sendMessage("The participant limit has not been reached yet. Please wait for more participants.").queue();
            return;
        }

        if (!isMiddleman(event.getAuthor().getId())) {
            event.getChannel().sendMessage("You are not authorized to start the jackpot. Only middlemen can use this command.").queue();
            return;
        }

        event.getChannel().sendMessage("Middleman approval received! Spinning the wheel...").queue();
        spinWheel(event);
    }

    private void spinWheel(MessageReceivedEvent event) {
        try {
            for (int i = 0; i < 3; i++) {
                event.getChannel().sendMessage("Spinning...").queue();
                Thread.sleep(1000);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Random rand = new Random();
        String winner = participants.get(rand.nextInt(participants.size()));

        event.getChannel().sendMessage("Congratulations " + winner + "! You have won the jackpot!").queue();
        try {
            Thread.sleep(1000);
            event.getChannel().sendMessage("Share your winnings in the winnings channel!").queue();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        // Reset the jackpot status
        resetJackpot();
    }

    private void stopJackpot(MessageReceivedEvent event) {
        if (jackpotActive) {
            event.getChannel().sendMessage("Jackpot event has been stopped.").queue();
            resetJackpot();
        } else {
            event.getChannel().sendMessage("There is no active jackpot event to stop.").queue();
        }
    }

    private void resetJackpot() {
        jackpotActive = false;
        middlemanAllowed = false;
        requiredUsersCount = 0;
        participants.clear();
    }
}
