package Events;

import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class Utility extends ListenerAdapter {
    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split(" ", 2);

        // Only respond to the special user
        long userId = event.getAuthor().getIdLong();
        if (userId != 700580614029574235L) return; // Ignore other users

        // Check if the message is "hmm"
        if (event.getMessage().getContentRaw().equalsIgnoreCase("hmm")) {
            event.getChannel().sendMessage("i am buying pokemon for my money ok and this is my pokemon i am i am deleted this is my problem why are you complimenting everything about it. that's is not your problem okay if you buy message me and if you not buy didn't pm ok and for you FF").queue();
            return;
        }

        // Handle ?purge command
        if (parts.length < 2 || !parts[0].equalsIgnoreCase("?purge")) {
            return;
        }

        String argument = parts[1];

        // Handle purge "all"
        if (argument.equalsIgnoreCase("all")) {
            event.getChannel().getIterableHistory().queue(messages -> {
                event.getChannel().purgeMessages(messages);
                event.getChannel().sendMessage("All messages have been purged.").queue();
            });
        } else {
            // Handle purge <number>
            try {
                int count = Integer.parseInt(argument);
                if (count <= 0) {
                    event.getChannel().sendMessage("Please specify a positive number of messages to purge.").queue();
                    return;
                }

                event.getChannel().getIterableHistory().takeAsync(count).thenAccept(messages -> {
                    event.getChannel().purgeMessages(messages);
                    event.getChannel().sendMessage("Purged " + count + " messages.").queue();
                });
            } catch (NumberFormatException e) {
                event.getChannel().sendMessage("Invalid number format. Usage: ?purge <number of messages / all>").queue();
            }
        }
    }
}
