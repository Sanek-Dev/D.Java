/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.intents;

import java.util.ArrayList;
import java.util.List;

public class DiscordIntents {
    public static List<DiscordIntent> getAllIntents() {
        return List.of(DiscordIntent.values());
    }

    public static List<DiscordIntent> getNonPrivilegedIntents() {
        List<DiscordIntent> list = new ArrayList<>();

        for(DiscordIntent intent: DiscordIntent.values()) {
            if(intent != DiscordIntent.MESSAGE_CONTENT && intent != DiscordIntent.GUILD_MEMBERS && intent != DiscordIntent.GUILD_PRESENCES) {
                list.add(intent);
            }
        }

        return list;
    }

    public static List<DiscordIntent> getPrivilegedIntents() {
        return List.of(DiscordIntent.MESSAGE_CONTENT, DiscordIntent.GUILD_MEMBERS, DiscordIntent.GUILD_PRESENCES);
    }

    public static List<DiscordIntent> getAllWithout(DiscordIntent... failIntents) {
        List<DiscordIntent> list = new ArrayList<>();

        for(DiscordIntent intent: DiscordIntent.values()) {
            for(DiscordIntent intentFail: failIntents) {
                if(intent != intentFail) {
                    list.add(intent);
                }
            }
        }

        return list;
    }
}
