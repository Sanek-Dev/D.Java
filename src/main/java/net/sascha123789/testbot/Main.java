package net.sascha123789.testbot;

import net.sascha123789.djava.ipc.DiscordIPC;
import net.sascha123789.djava.ipc.IPCEventListener;
import net.sascha123789.djava.ipc.entities.Activity;
import net.sascha123789.djava.ipc.entities.Button;
import net.sascha123789.djava.ipc.entities.PresenceAssets;
import net.sascha123789.djava.ipc.entities.PresenceTimestamps;

public class Main {
    public static void main(String[] args) {
        /*DiscordClientBuilder builder = new DiscordClientBuilder("MTA5ODk4OTMyNDIzOTkwMDc3NA.GvNCLP.sB4NidxkNdV5Hp42nKWbphqKrvT6zyKvflWu68");
        //builder.setDebug(true);
        builder.setRecommendedShardCount();
        builder.setIntents(DiscordIntents.getAllIntents());
        builder.setStatus(DiscordStatus.DO_NOT_DISTURB);
        builder.addActivity(new Activity("YouTube", ActivityType.WATCHING));

        DiscordClient client = builder.build();
        client.addEventAdapter(new Events());
        client.run();*/

        DiscordIPC ipc = DiscordIPC.connectToPipe("1024665267340587049", new IPCEventListener() {
            @Override
            public void onReady(DiscordIPC client) {
                PresenceAssets assets = new PresenceAssets()
                        .setLargeImage("win-logo")
                        .setLargeText("Build 22H2");

                Activity activity = new Activity(true)
                        .setAssets(assets)
                        .setDetails("Чисто Windows 11 22H2")
                        .setState("Смотрит Ютуб")
                        .setTimestamps(new PresenceTimestamps())
                        .setButton1(new Button("Tojex Community", "https://discord.gg/KUe5jGNbJ9"))
                        .setButton2(new Button("Мой Ютуб", "https://www.youtube.com/channel/UCqrrQ5JzfhAIc5-Ar-7OutA"));

                client.setPresence(activity);
                System.out.println("Ready!");
            }
        });
    }
}
