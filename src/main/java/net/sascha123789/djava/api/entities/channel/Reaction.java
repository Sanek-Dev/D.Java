/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.gateway.DiscordClient;

public class Reaction {
    private int count;
    private boolean me;
    private Emoji emoji;

    private Reaction(int count, boolean me, Emoji emoji) {
        this.count = count;
        this.me = me;
        this.emoji = emoji;
    }

    public static Reaction fromJson(DiscordClient client, JsonObject json) {
        int count = json.get("count").getAsInt();
        boolean me = json.get("me").getAsBoolean();
        Emoji emoji = Emoji.fromJson(client, json.get("emoji").getAsJsonObject());

        return new Reaction(count, me, emoji);
    }

    /**
     * @return Times this emoji has been used to react**/
    public int getCount() {
        return count;
    }

    /**
     * @return Whether the current user reacted using this emoji**/
    public boolean isMe() {
        return me;
    }

    /**
     * @return Emoji information**/
    public Emoji getEmoji() {
        return emoji;
    }
}
