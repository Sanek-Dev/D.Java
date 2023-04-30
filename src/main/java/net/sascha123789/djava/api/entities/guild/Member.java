/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class Member {
    private User user;
    private String nickname;
    private String avatar;
    private Set<Role> roles;
    private Timestamp joinedAt;
    private Timestamp boostStartTimestamp;
    private boolean deaf;
    private boolean mute;
    private Set<MemberFlag> flags;
    private boolean pending;
    private Set<DiscordPermission> permissions;
    private Timestamp timeoutUntil;
    private DiscordClient client;

    private Member(DiscordClient client, User user, String nickname, String avatar, Set<Role> roles, Timestamp joinedAt, Timestamp boostStartTimestamp, boolean deaf, boolean mute, Set<MemberFlag> flags, boolean pending, Set<DiscordPermission> permissions, Timestamp timeoutUntil) {
        this.client = client;
        this.user = user;
        this.nickname = nickname;
        this.avatar = avatar;
        this.roles = roles;
        this.joinedAt = joinedAt;
        this.boostStartTimestamp = boostStartTimestamp;
        this.deaf = deaf;
        this.mute = mute;
        this.flags = flags;
        this.pending = pending;
        this.timeoutUntil = timeoutUntil;
        this.permissions = permissions;
    }

    public User getUser() {
        return user;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarHash() {
        return avatar;
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public Timestamp getJoinedAt() {
        return joinedAt;
    }

    public Timestamp getBoostStartTimestamp() {
        return boostStartTimestamp;
    }

    public boolean isDeaf() {
        return deaf;
    }

    public boolean isMute() {
        return mute;
    }

    public Set<MemberFlag> getFlags() {
        return flags;
    }

    public boolean isPending() {
        return pending;
    }

    public Set<DiscordPermission> getPermissions() {
        return permissions;
    }

    public Timestamp getTimeoutUntil() {
        return timeoutUntil;
    }

    public static Member fromJson(DiscordClient client, JsonObject json) {
        User user = null;
        if(json.get("user") != null) {
            if(!json.get("user").isJsonNull()) {
                user = Constants.GSON.fromJson(json.get("user").getAsJsonObject(), User.class);
            }
        }

        String nick = "";
        if(json.get("nick") != null) {
            if(!json.get("nick").isJsonNull()) {
                nick = json.get("nick").getAsString();
            }
        }

        String avatar = "";
        if(json.get("avatar") != null) {
            if(!json.get("avatar").isJsonNull()) {
                avatar = json.get("avatar").getAsString();
            }
        }

        Set<Role> roles = new HashSet<>(); //TODO: Rework roles field
        //JsonArray arr = json.get("roles").getAsJsonArray();
        Timestamp joinedAt = null;
        if(json.get("joined_at") != null) {
            if(!json.get("joined_at").isJsonNull()) {
                String s = json.get("joined_at").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                joinedAt = Timestamp.valueOf(s);
            }
        }

        Timestamp boostStartTimestamp = null;
        if(json.get("premium_since") != null) {
            if(!json.get("premium_since").isJsonNull()) {
                String s = json.get("premium_since").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                boostStartTimestamp = Timestamp.valueOf(s);
            }
        }

        boolean deaf = false;
        if(json.get("deaf") != null) {
            if(!json.get("deaf").isJsonNull()) {
                deaf = json.get("deaf").getAsBoolean();
            }
        }

        boolean mute = false;
        if(json.get("mute") != null) {
            if(!json.get("mute").isJsonNull()) {
                mute = json.get("mute").getAsBoolean();
            }
        }

        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isJsonNull()) {
                flagsRaw = json.get("flags").getAsLong();
            }
        }

        Set<MemberFlag> flags = new HashSet<>();
        for(MemberFlag el: MemberFlag.values()) {
            if((el.getCode() & flagsRaw) == el.getCode()) {
                flags.add(el);
            }
        }

        boolean pending = false;
        if(json.get("pending") != null) {
            if(!json.get("pending").isJsonNull()) {
                pending = json.get("pending").getAsBoolean();
            }
        }

        Timestamp timeoutUntil = null;
        if(json.get("communication_disabled_until") != null) {
            if(!json.get("communication_disabled_until").isJsonNull()) {
                String s = json.get("communication_disabled_until").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                timeoutUntil = Timestamp.valueOf(s);
            }
        }

        long rawPerms = 0;
        if(json.get("permissions") != null) {
            if(!json.get("permissions").isJsonNull()) {
                rawPerms = Long.parseLong(json.get("permissions").getAsString());
            }
        }

        Set<DiscordPermission> perms = new HashSet<>();
        for(DiscordPermission el: DiscordPermission.values()) {
            if((el.getCode() & rawPerms) == el.getCode()) {
                perms.add(el);
            }
        }

        return new Member(client, user, nick, avatar, roles, joinedAt, boostStartTimestamp, deaf, mute, flags, pending, perms, timeoutUntil);
    }
}
