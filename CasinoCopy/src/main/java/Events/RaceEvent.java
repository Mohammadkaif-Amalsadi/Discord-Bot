package Events;

import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.emoji.Emoji;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class RaceEvent extends ListenerAdapter {

    private static final List<String> ALLOWED_CHANNEL_IDS = Arrays.asList("1323664415618236416", "1323664289755824260");
    private static final List<String> ANIMALS = Arrays.asList("🦀", "🐞", "🐢", "🐇", "🦄", "🐎", "🐝", "🐼", "🐨", "🐯");
    private static final int JOIN_DURATION = 15; // seconds
    private static final int RACE_INTERVAL = 2; // seconds

    private final Map<String, Set<User>> activeRaces = new HashMap<>();
    private final Map<User, String> userAnimalMap = new HashMap<>();
    private final Map<String, Boolean> raceInProgress = new HashMap<>();
    private final Random random = new Random();

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        String[] args = event.getMessage().getContentRaw().split("\\s+");

        if (args.length > 0 && (args[0].equalsIgnoreCase("c!race") || args[0].equalsIgnoreCase("join"))) {
            if (!isAllowedChannel(event.getChannel().getId())) {
                event.getChannel().sendMessage("Please use one of the allowed channels for this command: <#1249272470381527050> or <#1249693294229852191>").queue();
                return;
            }
        }

        if (args[0].equalsIgnoreCase("c!race")) {
            if (activeRaces.containsKey(event.getChannel().getId())) {
                event.getChannel().sendMessage("A race is already in progress in this channel!").queue();
                return;
            }

            Set<User> participants = new HashSet<>();
            activeRaces.put(event.getChannel().getId(), participants);
            raceInProgress.put(event.getChannel().getId(), false);

            event.getChannel().sendMessage("**A race has started!** Type `join` to participate. You have **15 seconds** to join!").queue();
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.schedule(() -> {
                if (participants.size() < 2) {
                    event.getChannel().sendMessage("**Not enough participants for the race. Race canceled.**").queue();
                    activeRaces.remove(event.getChannel().getId());
                    raceInProgress.remove(event.getChannel().getId());
                } else {
                    raceInProgress.put(event.getChannel().getId(), true);
                    startRace(event.getChannel().getId(), participants, event);
                }
            }, JOIN_DURATION, TimeUnit.SECONDS);

        } else if (args[0].equalsIgnoreCase("join")) {
            if (raceInProgress.getOrDefault(event.getChannel().getId(), false)) {
                event.getChannel().sendMessage("The race has already started, you cannot join now!").queue();
                return;
            }

            Set<User> participants = activeRaces.get(event.getChannel().getId());
            if (participants != null) {
                User user = event.getAuthor();
                if (participants.contains(user)) {
                    event.getChannel().sendMessage(user.getName() + ", you have **already joined** the race!").queue();
                } else {
                    String animal = assignUniqueAnimal();
                    participants.add(user);
                    userAnimalMap.put(user, animal);
                    event.getChannel().sendMessage(user.getName() + " has joined the race with " + animal + "! (" + participants.size() + ")")
                            .queue(message -> message.addReaction(Emoji.fromUnicode("U+2705")).queue());
                }
            }
        }
    }

    private boolean isAllowedChannel(String channelId) {
        return ALLOWED_CHANNEL_IDS.contains(channelId);
    }

    private String assignUniqueAnimal() {
        List<String> availableAnimals = new ArrayList<>(ANIMALS);
        availableAnimals.removeAll(userAnimalMap.values());
        return availableAnimals.get(random.nextInt(availableAnimals.size()));
    }

    private void startRace(String channelId, Set<User> participants, MessageReceivedEvent event) {
        Map<User, Integer> progressMap = new HashMap<>();
        Map<User, Integer> positionsMap = new HashMap<>();
        for (User participant : participants) {
            progressMap.put(participant, 0);
        }

        event.getChannel().sendMessage("```|🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🔚|```").queue(raceMessage -> {
            ScheduledExecutorService raceScheduler = Executors.newScheduledThreadPool(1);
            raceScheduler.scheduleAtFixedRate(() -> {
                StringBuilder raceStatus = new StringBuilder("```|🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🔚|\n");
                boolean raceOver = true;

                for (User participant : participants) {
                    int progress = progressMap.get(participant);
                    if (progress < 100) {
                        progress += random.nextInt(15) + 1;
                        if (progress > 100) {
                            progress = 100;
                        }
                        progressMap.put(participant, progress);
                    }

                    raceStatus.append(String.format("%3d%%|", progress));
                    for (int i = 0; i < progress / 2; i++) {
                        raceStatus.append("‣");
                    }
                    raceStatus.append(userAnimalMap.get(participant));
                    if (progress == 100 && !positionsMap.containsKey(participant)) {
                        int position = positionsMap.size() + 1;
                        positionsMap.put(participant, position);
                        raceStatus.append(" #").append(position);
                        if (position == 1) {
                            event.getChannel().sendMessage("🎉 **" + participant.getName() + " wins the race with " + userAnimalMap.get(participant) + " 🏆!** 🎉").queue();
                        }
                    } else if (positionsMap.containsKey(participant)) {
                        raceStatus.append(" #").append(positionsMap.get(participant));
                    }
                    raceStatus.append("\n");

                    if (progress < 100) {
                        raceOver = false;
                    }
                }

                raceStatus.append("|🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🏁🔚|```");

                raceMessage.editMessage(raceStatus.toString()).queue();

                if (raceOver) {
                    raceScheduler.shutdown();
                    activeRaces.remove(channelId);
                    raceInProgress.remove(channelId);
                    userAnimalMap.clear();
                }

            }, 0, RACE_INTERVAL, TimeUnit.SECONDS);
        });
    }
}
