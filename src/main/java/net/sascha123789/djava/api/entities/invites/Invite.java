/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.invites;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.ChannelUtils;
import net.sascha123789.djava.utils.Constants;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;

public class Invite {
    private DiscordClient client;
    private String code;
    //TODO: Add guild variable
    private BaseChannel channel;
    private User inviter;
    private LocalDateTime expirationDate;
    private InviteMetadata metadata;
    //TODO: Add Scheduled event support

    private Invite(DiscordClient client, String code, BaseChannel channel, User inviter, LocalDateTime expirationDate, InviteMetadata metadata) {
        this.client = client;
        this.code = code;
        this.channel = channel;
        this.inviter = inviter;
        this.expirationDate = expirationDate;
        this.metadata = metadata;
    }

    public static Invite fromJson(DiscordClient client, JsonObject json) {
        String code = json.get("code").getAsString();
        BaseChannel channel = null;
        if(json.get("channel") != null) {
            if(!json.get("channel").isJsonNull()) {
                channel = ChannelUtils.switchTypes(client, json.get("channel").getAsJsonObject());
            }
        }

        User inviter = null;
        if(json.get("inviter") != null) {
            if(!json.get("inviter").isJsonNull()) {
                inviter = Constants.GSON.fromJson(json.get("inviter").getAsJsonObject(), User.class);
            }
        }

        LocalDateTime expiresAt = null;
        if(json.get("expires_at") != null) {
            if(!json.get("expires_at").isJsonNull()) {
                String s = json.get("expires_at").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                Timestamp timestamp = Timestamp.valueOf(s);
                expiresAt = timestamp.toLocalDateTime();
            }
        }

        return new Invite(client, code, channel, inviter, expiresAt, InviteMetadata.fromJson(json));
    }

    public InviteMetadata getMetadata() {
        return metadata;
    }

    public String getCode() {
        return code;
    }

    public BaseChannel getChannel() {
        return channel;
    }

    public User getInviter() {
        return inviter;
    }

    public LocalDateTime getExpirationDate() {
        return expirationDate;
    }
}
