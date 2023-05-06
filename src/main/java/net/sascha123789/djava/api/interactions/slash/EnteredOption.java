/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.fasterxml.jackson.databind.JsonNode;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.Attachment;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.gateway.DiscordClient;

public class EnteredOption {
    private SlashCommandOptionType type;
    private String name;
    private JsonNode value;
    private String guildId;
    private DiscordClient client;

    public EnteredOption(SlashCommandOptionType type, String name, JsonNode value, DiscordClient client, String guildId) {
        this.type = type;
        this.name = name;
        this.value = value;
        this.client = client;
        this.guildId = guildId;
    }

    public Role getValueAsRole() {
        return Role.fromJson(client, value);
    }

    public Attachment getValueAsAttachment() {
        return Attachment.fromJson(value);
    }

    public BaseChannel getValueAsChannel() {
        return client.getCacheManager().getChannelCache().getUnchecked(value.get("id").asText());
    }

    public Member getValueAsMember() {
        return client.getCacheManager().getMemberCache().getUnchecked(guildId + ":" + getValueAsUser().getId());
    }

    public User getValueAsUser() {
        User user = null;

        try {
            user = User.fromJson(value);
        } catch(Exception e) {
            user = getValueAsMember().getUser();
        }

        return user;
    }

    public boolean getValueAsBoolean() {
        return value.asBoolean();
    }

    public String getValueAsString() {
        return value.asText();
    }

    public long getValueAsLong() {
        return value.asLong();
    }

    public int getValueAsInt() {
        return value.asInt();
    }

    public double getValueAsDouble() {
        return value.asDouble();
    }

    public SlashCommandOptionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
