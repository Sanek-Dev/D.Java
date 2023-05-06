/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.ChannelUtils;
import net.sascha123789.djava.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class GuildWidget implements Identifiable {
    private String id;
    private String name;
    private String instantInvite;
    private Set<BaseChannel> channels;
    private Set<User> members;
    private int presenceCount;
    private DiscordClient client;

    private GuildWidget(DiscordClient client, String id, String name, String instantInvite, Set<BaseChannel> channels, Set<User> members, int presenceCount) {
        this.id = id;
        this.name = name;
        this.instantInvite = instantInvite;
        this.channels = channels;
        this.members = members;
        this.presenceCount = presenceCount;
        this.client = client;
    }

    public static GuildWidget fromJson(DiscordClient client, JsonNode json) {
        String id = json.get("id").asText();
        String name = json.get("name").asText();
        String instantInvite = "";
        if(json.get("instant_invite") != null) {
            if(!json.get("instant_invite").isNull()) {
                instantInvite = json.get("instant_invite").asText();
            }
        }

        Set<BaseChannel> channels = new HashSet<>();
        JsonNode channelsRaw = json.get("channels");
        for(JsonNode el: channelsRaw) {
            channels.add(ChannelUtils.switchTypes(client, el));
        }

        Set<User> members = new HashSet<>();
        JsonNode membersRaw = json.get("members");
        for(JsonNode el: membersRaw) {
            members.add(User.fromJson(el));
        }

        int presenceCount = json.get("presence_count").asInt();

        return new GuildWidget(client, id, name, instantInvite, channels, members, presenceCount);
    }

    public String getId() {
        return id;
    }

    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }

    public String getName() {
        return name;
    }

    public String getInstantInvite() {
        return instantInvite;
    }

    public Set<BaseChannel> getChannels() {
        return channels;
    }

    public Set<User> getMembers() {
        return members;
    }

    public int getPresenceCount() {
        return presenceCount;
    }
}
