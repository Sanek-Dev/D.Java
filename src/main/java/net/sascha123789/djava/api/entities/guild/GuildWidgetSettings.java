/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class GuildWidgetSettings {
    private boolean enabled;
    private BaseChannel channel;

    private GuildWidgetSettings(DiscordClient client, String channelId, boolean enabled) {
        this.enabled = enabled;
        this.channel = null;
        if(!channelId.isEmpty()) {
            this.channel = client.getChannelById(channelId).get();
        }
    }

    public static GuildWidgetSettings fromJson(DiscordClient client, JsonNode json) {
        boolean enabled = json.get("enabled").asBoolean();
        String channelId = "";
        if(json.get("channel_id") != null) {
            if(!json.get("channel_id").isNull()) {
                channelId = json.get("channel_id").asText();
            }
        }

        return new GuildWidgetSettings(client, channelId, enabled);
    }

    public boolean isEnabled() {
        return enabled;
    }

    public BaseChannel getChannel() {
        return channel;
    }
}
