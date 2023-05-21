/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.internal.bind.util.ISO8601Utils;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.VoiceChannel;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

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
    private Guild guild;

    private Member(Guild guild, DiscordClient client, User user, String nickname, String avatar, Set<Role> roles, Timestamp joinedAt, Timestamp boostStartTimestamp, boolean deaf, boolean mute, Set<MemberFlag> flags, boolean pending, Set<DiscordPermission> permissions, Timestamp timeoutUntil) {
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
        this.guild = guild;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;

        if(obj.getClass() != this.getClass()) return false;

        return this.user.getId().equals(((Member) obj).getUser().getId());
    }

    @Override
    public int hashCode() {
        return this.user.getId().hashCode();
    }

    public void kick(String reason) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + this.guild.getId() + "/members/" + this.user.getId())
                .addHeader("X-Audit-Log-Reason", reason)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void kick() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + this.guild.getId() + "/members/" + this.user.getId())
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void editFlags(MemberFlag... flags) {
        long bit = 0;

        for(MemberFlag el: flags) {
            bit += el.getCode();
        }

        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("flags", bit);

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void moveToVoice(VoiceChannel channel) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("channel_id", channel.getId());

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setDeaf(boolean deaf) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("deaf", deaf);

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setMute(boolean mute) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("mute", mute);

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void timeout(TimeUnit unit, long n) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("communication_disabled_until", ISO8601Utils.format(new Date(new Date().getTime() + unit.toMillis(n)), true));

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void removeRole(Role role) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + this.guild.getId() + "/members/" + this.user.getId() + "/roles/" + role.getId())
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void addRole(Role role) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + this.guild.getId() + "/members/" + this.user.getId() + "/roles/" + role.getId())
                .put(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void setNickname(String nickname) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("nick", nickname);

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId() + "/members/" + user.getId())
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public User getUser() {
        return user;
    }

    public String getNickname() {
        return nickname;
    }

    /**
     * @apiNote Default image type is png**/
    public String getAvatarUrl() {
        return Constants.BASE_IMAGES_URL + "guilds/" + guild.getId() + "/users/" + user.getId() + "/avatars/" + avatar + ".png";
    }

    public String getAvatarUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "guilds/" + guild.getId() + "/users/" + user.getId() + "/avatars/" + avatar + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    public String getAvatarHash() {
        return avatar;
    }

    public ImmutableSet<Role> getRoles() {
        return ImmutableSet.copyOf(roles);
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

    public ImmutableSet<MemberFlag> getFlags() {
        return ImmutableSet.copyOf(flags);
    }

    public boolean isPending() {
        return pending;
    }

    public ImmutableSet<DiscordPermission> getPermissions() {
        return ImmutableSet.copyOf(permissions);
    }

    public Timestamp getTimeoutUntil() {
        return timeoutUntil;
    }

    public static Member fromJson(DiscordClient client, JsonNode json, Guild guild) {

        User user = null;
        if(json.get("user") != null) {
            if(!json.get("user").isNull()) {
                user = User.fromJson(json.get("user"));
            }
        }

        String nick = "";
        if(json.get("nick") != null) {
            if(!json.get("nick").isNull()) {
                nick = json.get("nick").asText();
            }
        }

        String avatar = "";
        if(json.get("avatar") != null) {
            if(!json.get("avatar").isNull()) {
                avatar = json.get("avatar").asText();
            }
        }

        Set<Role> roles = new HashSet<>();
        JsonNode arr = json.get("roles");

        for(JsonNode el: arr) {
            String id = el.asText();
            roles.add(guild.getRoleById(id));
        }

        Timestamp joinedAt = null;
        if(json.get("joined_at") != null) {
            if(!json.get("joined_at").isNull()) {
                String s = json.get("joined_at").asText();
                s = StringUtils.replace(s,  "+00:00", "");
                s = StringUtils.replace(s, "T", " ");
                joinedAt = Timestamp.valueOf(s);
            }
        }

        Timestamp boostStartTimestamp = null;
        if(json.get("premium_since") != null) {
            if(!json.get("premium_since").isNull()) {
                String s = json.get("premium_since").asText();
                s = StringUtils.replace(s, "+00:00", "");
                s = StringUtils.replace(s, "T", " ");
                boostStartTimestamp = Timestamp.valueOf(s);
            }
        }

        boolean deaf = false;
        if(json.get("deaf") != null) {
            if(!json.get("deaf").isNull()) {
                deaf = json.get("deaf").asBoolean();
            }
        }

        boolean mute = false;
        if(json.get("mute") != null) {
            if(!json.get("mute").isNull()) {
                mute = json.get("mute").asBoolean();
            }
        }

        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isNull()) {
                flagsRaw = json.get("flags").asLong();
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
            if(!json.get("pending").isNull()) {
                pending = json.get("pending").asBoolean();
            }
        }

        Timestamp timeoutUntil = null;
        if(json.get("communication_disabled_until") != null) {
            if(!json.get("communication_disabled_until").isNull()) {
                String s = json.get("communication_disabled_until").asText();
                s = StringUtils.replace(s, "+00:00", "");
                s = StringUtils.replace(s, "T", " ");
                timeoutUntil = Timestamp.valueOf(s);
            }
        }

        long rawPerms = 0;
        if(json.get("permissions") != null) {
            if(!json.get("permissions").isNull()) {
                rawPerms = Long.parseLong(json.get("permissions").asText());
            }
        }

        Set<DiscordPermission> perms = new HashSet<>();
        for(DiscordPermission el: DiscordPermission.values()) {
            if((el.getCode() & rawPerms) == el.getCode()) {
                perms.add(el);
            }
        }

        return new Member(guild, client, user, nick, avatar, roles, joinedAt, boostStartTimestamp, deaf, mute, flags, pending, perms, timeoutUntil);
    }
}
