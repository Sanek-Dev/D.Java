/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.gateway.DiscordClient;

public class GuildWelcomeScreenChannel {
    private BaseChannel channel;
    private String description;
    private Emoji emoji;
    private DiscordClient client;

    private GuildWelcomeScreenChannel(DiscordClient client, String channelId, String description, String emojiId, String emojiName) {
        this.client = client;
        this.channel = client.getChannelById(channelId).get();
        this.description = description;
        this.emoji = null;

        if(emojiId == null && !emojiName.isEmpty()) {
            emoji = Emoji.fromString(emojiName);
        } else {
            emoji = Emoji.fromId(client, channel.getGuildId(), emojiId);
        }
    }

    public static GuildWelcomeScreenChannel fromJson(DiscordClient client, JsonObject json) {
        String channelId = "";
        if(json.get("channel_id") != null) {
            if(!json.get("channel_id").isJsonNull()) {
                channelId = json.get("channel_id").getAsString();
            }
        }

        String description = "";
        if(json.get("description") != null) {
            if(!json.get("description").isJsonNull()) {
                description = json.get("description").getAsString();
            }
        }

        String emojiId = null;
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isJsonNull()) {
                emojiId = json.get("emoji_id").getAsString();
            }
        }

        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isJsonNull()) {
                emojiId = json.get("emoji_name").getAsString();
            }
        }

        return new GuildWelcomeScreenChannel(client, channelId, description, emojiId, emojiName);
    }

    public BaseChannel getChannel() {
        return channel;
    }

    public String getDescription() {
        return description;
    }

    public Emoji getEmoji() {
        return emoji;
    }
}
