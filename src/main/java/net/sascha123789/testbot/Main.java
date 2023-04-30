package net.sascha123789.testbot;

import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.gateway.DiscordClientBuilder;
import net.sascha123789.djava.gateway.intents.DiscordIntents;
import net.sascha123789.djava.gateway.presence.ActivityType;
import net.sascha123789.djava.gateway.presence.DiscordStatus;

public class Main {
    public static void main(String[] args) {

        DiscordClientBuilder builder = new DiscordClientBuilder("MTA5ODk4OTMyNDIzOTkwMDc3NA.GWYlqR.z68JYQb1ZMfAUJIwu-O7FIppC7vRDAVKZxI0yE");
        builder.setRecommendedShardCount();
        builder.setIntents(DiscordIntents.getAllIntents());
        builder.setStatus(DiscordStatus.DO_NOT_DISTURB);
        builder.addActivity(new net.sascha123789.djava.gateway.presence.Activity("YouTube", ActivityType.WATCHING));
        builder.addEventAdapter(new Events());

        DiscordClient client = builder.build();
        client.run();

        /*DiscordIPC ipc = DiscordIPC.connectToPipe("1024665267340587049", new IPCEventListener() {
            @Override
            public void onReady(DiscordIPC client) {
                client.auth("Fbo7_cWaVU4WeMVSeF2O9M7JkC-efbPD");
            }

            @Override
            public void onAuth(DiscordIPC client, User user, String accessToken) {
                PresenceAssets assets = new PresenceAssets()
                        .setLargeImage("win-logo")
                        .setLargeText("Build 22H2")
                        .setSmallImage(user.getAvatarUrl())
                        .setSmallText(user.toString());

                Activity activity = new Activity(true)
                        .setAssets(assets)
                        .setDetails("Чисто Windows 11 22H2")
                        .setState("Смотрит Ютуб")
                        .setTimestamps(new PresenceTimestamps())
                        .setButton1(new Button("Tojex Community", "https://discord.gg/KUe5jGNbJ9"))
                        .setButton2(new Button("Мой Ютуб", "https://www.youtube.com/channel/UCqrrQ5JzfhAIc5-Ar-7OutA"));

                client.setPresence(activity);
                System.out.println("Welcome, " + user.toString() + "!");
            }
        });*/
    }
}
