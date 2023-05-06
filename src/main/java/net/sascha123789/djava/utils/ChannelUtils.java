/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.channel.*;
import net.sascha123789.djava.gateway.DiscordClient;

public class ChannelUtils {
    public static BaseChannel switchTypes(DiscordClient client, JsonNode json) {
        int type = json.get("type").asInt();

        return switch (type) {
            case 0, 5 -> TextChannel.fromJson(client, json);
            case 2, 13 -> VoiceChannel.fromJson(client, json);
            case 4, 14 -> CategoryChannel.fromJson(client, json);
            case 10, 12, 11 -> ThreadChannel.fromJson(client, json);
            case 15 -> ForumChannel.fromJson(client, json);
            default -> null;
        };
    }
}
