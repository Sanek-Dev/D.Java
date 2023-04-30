/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

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

    public static GuildWelcomeScreen fromJson(DiscordClient client, JsonObject json) {
        String desc = "";
        if(json.get("description") != null) {
            if(!json.get("description").isJsonNull()) {
                desc = json.get("description").getAsString();
            }
        }

        List<GuildWelcomeScreenChannel> channels = new ArrayList<>();
        if(json.get("welcome_channels") != null) {
            if(!json.get("welcome_channels").isJsonNull()) {
                JsonArray arr = json.get("welcome_channels").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();

                    channels.add(GuildWelcomeScreenChannel.fromJson(client, o));
                }
            }
        }

        return new GuildWelcomeScreen(client, desc, channels);
    }

    public String getDescription() {
        return description;
    }

    public List<GuildWelcomeScreenChannel> getChannels() {
        return channels;
    }
}
