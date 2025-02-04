import Events.*;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscBot extends ListenerAdapter {


    //private static final String BOT_TOKEN = "MTEwODI3MzIwNzA0OTQ2NTg2Ng.G6AXcw.6F0iL1GbIOH_edFDdPEPJuEEzMKFLfc5vFcvd8";
    private static final String BOT_TOKEN = "Your bot token goes here";
    private static final String AUTHORIZED_USER_ID = "700580614029574235";
    private static JDA jda;

    public static void main(String[] args) {
        jda = JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .setActivity(Activity.playing("!!!"))
                .addEventListeners(new Event1(), new Event2(), new Event3(), new Event4(),new RaceEvent(), new DiscBot(),new Utility())
                .build();
    }

    private void initBot() {
        JDABuilder.createDefault(BOT_TOKEN)
                .enableIntents(GatewayIntent.MESSAGE_CONTENT)
                .addEventListeners(this)
                .build();
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        System.out.println("Message: "+event.getMessage().getContentRaw());
        if (event.getAuthor().isBot()) return;

        String msgContent = event.getMessage().getContentRaw();
        if (msgContent.startsWith("c!timer")) {
            handleTimerCommand(event);
        }
        else if (msgContent.startsWith("c!random")) {
            handleRandomCommand(event);
        }
    }

    private void handleRandomCommand(MessageReceivedEvent event) {
        String[] parts = event.getMessage().getContentRaw().split(" ");
        if (parts.length != 2) {
            event.getChannel().sendMessage("Invalid command format. Use c!random @userId").queue();
            return;
        }


    }   

    private void handleTimerCommand(MessageReceivedEvent event) {
        if (!event.getAuthor().getId().equals(AUTHORIZED_USER_ID)) {
            event.getChannel().sendMessage("You are not authorized to use this command.").queue();
            return;
        }

        String[] parts = event.getMessage().getContentRaw().split(" ");
        if (parts.length != 2) {
            event.getChannel().sendMessage("Invalid command format. Use c!timer <duration>").queue();
            return;
        }

        String duration = parts[1];
        long delay;
        TimeUnit unit;

        if (duration.endsWith("seconds")) {
            delay = Long.parseLong(duration.replace("seconds", "").trim());
            unit = TimeUnit.SECONDS;
        } else if (duration.endsWith("minutes")) {
            delay = Long.parseLong(duration.replace("minutes", "").trim());
            unit = TimeUnit.MINUTES;
        } else if (duration.endsWith("hour")) {
            delay = Long.parseLong(duration.replace("hour", "").trim());
            unit = TimeUnit.HOURS;
        } else {
            event.getChannel().sendMessage("Invalid duration format. Use c!timer 30seconds, c!timer 30minutes, or c!timer 1hour").queue();
            return;
        }

        event.getChannel().sendMessage("Bot will shut down in " + delay + " " + unit.toString().toLowerCase()).queue();
        scheduleShutdown(delay, unit, event.getChannel().asTextChannel());
    }

    private void scheduleShutdown(long delay, TimeUnit unit, TextChannel channel) {
        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.schedule(() -> {
            if (jda != null) {
                channel.sendMessage("Bot is now shutting down.").queue();
                jda.shutdown();
            }
            System.out.println("Bot has been shut down.");
        }, delay, unit);
    }
}
