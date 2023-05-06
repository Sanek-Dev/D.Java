/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.gateway.DiscordClient;

import java.util.ArrayList;
import java.util.List;

public class GuildWelcomeScreen {
    private String description;
    private List<GuildWelcomeScreenChannel> channels;
    private DiscordClient client;

    private GuildWelcomeScreen(DiscordClient client, String description, List<GuildWelcomeScreenChannel> channels) {
        this.description = description;
        this.channels = channels;
        this.client = client;
    }

    public static GuildWelcomeScreen fromJson(DiscordClient client, JsonNode json) {
        String desc = "";
        if(json.get("description") != null) {
            if(!json.get("description").isNull()) {
                desc = json.get("description").asText();
            }
        }

        List<GuildWelcomeScreenChannel> channels = new ArrayList<>();
        if(json.get("welcome_channels") != null) {
            if(!json.get("welcome_channels").isNull()) {
                JsonNode arr = json.get("welcome_channels");

                for(JsonNode el: arr) {
                    channels.add(GuildWelcomeScreenChannel.fromJson(client, el));
                }
            }
        }

        return new GuildWelcomeScreen(client, desc, channels);
    }

    public String getDescription() {
        return description;
    }

    public ImmutableSet<GuildWelcomeScreenChannel> getChannels() {
        return ImmutableSet.copyOf(channels);
    }
}
