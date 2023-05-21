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
import net.sascha123789.djava.utils.Constants;

import java.util.Optional;

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

    public Optional<Role> getValueAsRole() {
        Role r = null;

        try {
            r = Role.fromJson(client, value);
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(r);
    }

    public Optional<Attachment> getValueAsAttachment() {
        Attachment a = null;

        try {
            a = Attachment.fromJson(value);
        } catch (Exception e) {
            return Optional.empty();
        }

        return Optional.of(a);
    }

    public Optional<BaseChannel> getValueAsChannel() {
        BaseChannel c = null;

        try {
            c = client.getCacheManager().getChannelCache().getUnchecked(value.get("id").asText());
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(c);
    }

    public Optional<Member> getValueAsMember() {
        Member m = null;

        try {
            m = client.getCacheManager().getMemberCache().getUnchecked(guildId + ":" + getValueAsUser().get().getId());
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(m);
    }

    public Optional<User> getValueAsUser() {
        User u = null;

        try {
            u = User.fromJson(value);
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(u);
    }

    public Optional<Boolean> getValueAsBoolean() {
        if(value.isNull()) {
            return Optional.empty();
        } else {
            return Optional.of(value.asBoolean());
        }
    }

    public Optional<String> getValueAsString() {
        String s = "";

        try {
            s = value.asText();
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(s);
    }

    public Optional<Long> getValueAsLong() {
        long l = 0;

        try {
            l = value.asLong();
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(l);
    }

    public Optional<Integer> getValueAsInt() {
        int i = 0;

        try {
            i = value.asInt();
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(i);
    }

    public Optional<Double> getValueAsDouble() {
        double d = 0;

        try {
            d = value.asDouble();
        } catch(Exception e) {
            return Optional.empty();
        }

        return Optional.of(d);
    }

    public SlashCommandOptionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
