/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
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

    public static GuildWelcomeScreenChannel fromJson(DiscordClient client, JsonNode json) {
        String channelId = "";
        if(json.get("channel_id") != null) {
            if(!json.get("channel_id").isNull()) {
                channelId = json.get("channel_id").asText();
            }
        }

        String description = "";
        if(json.get("description") != null) {
            if(!json.get("description").isNull()) {
                description = json.get("description").asText();
            }
        }

        String emojiId = null;
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isNull()) {
                emojiId = json.get("emoji_id").asText();
            }
        }

        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isNull()) {
                emojiId = json.get("emoji_name").asText();
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
