/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.invites.Invite;
import net.sascha123789.djava.api.entities.invites.InviteData;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class BaseChannel implements Identifiable {
    protected String id;
    protected ChannelType type;
    protected String guildId;
    protected int position;
    protected ImmutableSet<PermissionOverwrite> permissionOverwrites;
    protected String name;
    protected boolean nsfw;
    protected String parentId;
    protected ImmutableSet<ChannelFlag> flags;
    protected DiscordClient client;

    protected BaseChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags) {
        this.id = id;
        this.type = type;
        this.guildId = guildId;
        this.position = position;
        this.permissionOverwrites = ImmutableSet.copyOf(permissionOverwrites);
        this.name = name;
        this.nsfw = nsfw;
        this.parentId = parentId;
        this.flags = ImmutableSet.copyOf(flags);
        this.client = client;
    }

    public String toMention() {
        return "<#" + id + ">";
    }

    public Optional<Invite> createInvite(InviteData data) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/invites")
                .post(RequestBody.create(data.toJson().toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            return Optional.of(Invite.fromJson(client, Constants.MAPPER.readTree(res)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public MessageableChannel asMessageable() {
        return ((MessageableChannel) this);
    }

    public Optional<ImmutableSet<Invite>> getInvites() {
        Set<Invite> set = new HashSet<>();

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/invites")
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            JsonNode arr = Constants.MAPPER.readTree(res);

            for(JsonNode el: arr) {
                set.add(Invite.fromJson(client, el));
            }

            return Optional.of(ImmutableSet.copyOf(set));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    /**
     * @param id Id of user or role**/
    public void deletePermissionOverwrite(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/permissions/" + id)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void editPermissionOverwrite(PermissionOverwrite overwrite) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/permissions/" + overwrite.getId())
                .put(RequestBody.create(overwrite.toJson().toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String reason) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id)
                .addHeader("X-Audit-Log-Reason", reason)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();

            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();

            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ForumChannel asForumChannel() {
        return ((ForumChannel) this);
    }

    public CategoryChannel asCategoryChannel() {
        return ((CategoryChannel) this);
    }

    public ThreadChannel asThreadChannel() {
        return ((ThreadChannel) this);
    }

    public VoiceChannel asVoiceChannel() {
        return ((VoiceChannel) this);
    }

    public TextChannel asTextChannel() {
        return ((TextChannel) this);
    }

    public ChannelType getType() {
        return type;
    }

    public String getGuildId() {
        return guildId;
    }

    public int getPosition() {
        return position;
    }

    public ImmutableSet<PermissionOverwrite> getPermissionOverwrites() {
        return permissionOverwrites;
    }

    public String getName() {
        return name;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public String getParentId() {
        return parentId;
    }

    public ImmutableSet<ChannelFlag> getFlags() {
        return flags;
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
