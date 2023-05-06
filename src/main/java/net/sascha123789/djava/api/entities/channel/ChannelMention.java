/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.enums.ChannelType;

public class ChannelMention implements Identifiable {
    private String id;
    private String guildId;
    private ChannelType type;
    private String name;

    private ChannelMention(String id, String guildId, ChannelType type, String name) {
        this.id = id;
        this.guildId = guildId;
        this.type = type;
        this.name = name;
    }

    public String getGuildId() {
        return guildId;
    }

    public ChannelType getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public static ChannelMention fromJson(JsonNode json) {
        String id = json.get("id").asText();
        String guildId = json.get("guild_id").asText();
        int t = json.get("type").asInt();
        ChannelType type = (t == 0 ? ChannelType.TEXT : (t == 2 ? ChannelType.VOICE : (t == 4 ? ChannelType.CATEGORY : (t == 5 ? ChannelType.ANNOUNCEMENT : (t == 10 ? ChannelType.ANNOUNCEMENT : (t == 11 ? ChannelType.PUBLIC_THREAD : (t == 12 ? ChannelType.PRIVATE_THREAD : (t == 13 ? ChannelType.STAGE : (t == 14 ? ChannelType.DIRECTORY : ChannelType.FORUM)))))))));
        String name = json.get("name").asText();

        return new ChannelMention(id, guildId, type, name);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }
}
