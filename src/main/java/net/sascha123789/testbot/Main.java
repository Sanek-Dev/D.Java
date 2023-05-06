package net.sascha123789.testbot;

import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.gateway.intents.DiscordIntents;
import net.sascha123789.djava.gateway.presence.Activity;
import net.sascha123789.djava.gateway.presence.ActivityType;
import net.sascha123789.djava.gateway.presence.DiscordStatus;

public class Main {
    public static void main(String[] args) {
        DiscordClient.Builder builder = new DiscordClient.Builder(args[0])
                .setRecommendedShardCount()
                .setIntents(DiscordIntents.getAllIntents())
                .setStatus(DiscordStatus.DO_NOT_DISTURB)
                .addActivity(new Activity("YouTube", ActivityType.WATCHING))
                .addEventAdapter(new Events());

        DiscordClient client = builder.build();
        client.run();
    }
}
