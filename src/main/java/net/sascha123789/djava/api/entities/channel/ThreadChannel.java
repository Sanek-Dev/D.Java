/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class ThreadChannel extends MessageableChannel {
    private int msgCount;
    private int memberCount;
    private ThreadMetadata metadata;
    private int totalMsgSent;

    private ThreadChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags, int memberCount, int msgCount, ThreadMetadata metadata, int totalMsgSent) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
        this.msgCount = msgCount;
        this.metadata = metadata;
        this.memberCount = memberCount;
        this.totalMsgSent = totalMsgSent;
    }

    public Optional<Set<ThreadMember>> getThreadMembers() {
        HttpUrl.Builder url = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/thread-members").newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            JsonArray arr = Constants.GSON.fromJson(res, JsonArray.class);
            Set<ThreadMember> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();
                set.add(ThreadMember.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ThreadMember> getMemberById(String userId) {
        HttpUrl.Builder url = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/thread-members/" + userId).newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadMember.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void removeMember(String userId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/thread-members/" + userId)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addMember(String userId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/thread-members/" + userId)
                .put(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Updater getUpdater() {
        return new Updater(client, this);
    }

    public static class Updater {
        private DiscordClient client;
        private ThreadChannel channel;
        private String name;
        private int position;
        private Set<PermissionOverwrite> overwrites;
        private String parentId;
        private Set<ChannelFlag> flags;
        private boolean archived;
        private int archiveDuration;
        private boolean locked;
        private boolean invitable;
        private int invit;
        private int rateLimit;
        private Set<String> appliedTags;

        public Updater(DiscordClient client, ThreadChannel channel) {
            this.client = client;
            this.channel = channel;
            this.name = "";
            this.position = -1;
            this.overwrites = new HashSet<>();
            this.parentId = "";
            this.flags = new HashSet<>();
            this.archived = false;
            this.archiveDuration = -1;
            this.locked = false;
            this.invitable = false;
            this.invit = -1;
            this.rateLimit = -1;
            this.appliedTags = new HashSet<>();
        }

        public Updater removeAppliedTag(String id) {
            this.appliedTags.remove(id);
            return this;
        }

        public Updater addAppliedTag(String id) {
            this.appliedTags.add(id);
            return this;
        }

        public Updater setRateLimitPerUser(int rateLimit) {
            this.rateLimit = rateLimit;
            return this;
        }

        public Updater setInvitable(boolean invitable) {
            this.invitable = invitable;
            invit = 0;
            return this;
        }

        public Updater setLocked(boolean locked) {
            this.locked = locked;
            return this;
        }

        public Updater setArchiveDuration(int archiveDuration) {
            this.archiveDuration = archiveDuration;
            return this;
        }

        public Updater setArchived(boolean archived) {
            this.archived = archived;
            return this;
        }

        public Updater setFlags(Set<ChannelFlag> flags) {
            this.flags = flags;
            return this;
        }

        public Updater setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Updater removePermissionOverwrite(PermissionOverwrite overwrite) {
            this.overwrites.remove(overwrite);
            return this;
        }

        public Updater addPermissionOverwrite(PermissionOverwrite overwrite) {
            this.overwrites.add(overwrite);
            return this;
        }

        public Updater setPosition(int position) {
            this.position = position;
            return this;
        }


        public Updater setName(String name) {
            this.name = name;
            return this;
        }

        public void update() {
            JsonObject obj = new JsonObject();

            if (!name.isEmpty()) {
                obj.addProperty("name", name);
            }

            if (position != -1) {
                obj.addProperty("position", Math.abs(position));
            }

            if(archiveDuration != -1) {
                obj.addProperty("auto_archive_duration", Math.abs(archiveDuration));
            }

            obj.addProperty("locked", locked);

            if(invit == 0) {
                obj.addProperty("invitable", invitable);
            }

            if(rateLimit != -1) {
                obj.addProperty("rate_limit_per_user", Math.abs(rateLimit));
            }

            if(!appliedTags.isEmpty()) {
                JsonArray arr = new JsonArray();

                for(String el: appliedTags) {
                    arr.add(el);
                }

                obj.add("applied_tags", arr);
            }

            if (!overwrites.isEmpty()) {
                JsonArray arr = new JsonArray();

                for (PermissionOverwrite overwrite : overwrites) {
                    JsonObject o = overwrite.toJson();
                    arr.add(o);
                }

                obj.add("permission_overwrites", arr);
            }

            if (!parentId.isEmpty()) {
                obj.addProperty("parent_id", parentId);
            }
            obj.addProperty("archived", archived);

            if (!flags.isEmpty()) {
                long flags = 0;
                for (ChannelFlag el : this.flags) {
                    flags += el.getCode();
                }
                obj.addProperty("flags", flags);
            }

            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channel.getId())
                    .patch(RequestBody.create(obj.toString(), MediaType.parse("application/json"))).build();

            try(Response resp = client.getHttpClient().newCall(request).execute()) {
                String str = resp.body().string();

                ErrHandler.handle(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static ThreadChannel fromJson(DiscordClient client, JsonObject json) {
        /* Base */
        String id = json.get("id").getAsString();
        int t = json.get("type").getAsInt();
        ChannelType type = (t == 0 ? ChannelType.TEXT : (t == 2 ? ChannelType.VOICE : (t == 4 ? ChannelType.CATEGORY : (t == 5 ? ChannelType.ANNOUNCEMENT : (t == 10 ? ChannelType.ANNOUNCEMENT : (t == 11 ? ChannelType.PUBLIC_THREAD : (t == 12 ? ChannelType.PRIVATE_THREAD : (t == 13 ? ChannelType.STAGE : (t == 14 ? ChannelType.DIRECTORY : ChannelType.FORUM)))))))));
        String guildId = "";

        if(json.get("guild_id") != null) {
            if(!json.get("guild_id").isJsonNull()) {
                guildId = json.get("guild_id").getAsString();
            }
        }

        int position = 0;
        if(json.get("position") != null) {
            if(!json.get("position").isJsonNull()) {
                position = json.get("position").getAsInt();
            }
        }

        Set<PermissionOverwrite> overwrites = new HashSet<>();

        if(json.get("permission_overwrites") != null) {
            if(!json.get("permission_overwrites").isJsonNull()) {
                JsonArray arr = json.get("permission_overwrites").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();

                    overwrites.add(PermissionOverwrite.fromJson(o));
                }
            }
        }

        String name = "";
        if(json.get("name") != null) {
            if(!json.get("name").isJsonNull()) {
                name = json.get("name").getAsString();
            }
        }

        boolean nsfw = false;
        if(json.get("nsfw") != null) {
            if(!json.get("nsfw").isJsonNull()) {
                nsfw = json.get("nsfw").getAsBoolean();
            }
        }

        String parentId = "";
        if(json.get("parent_id") != null) {
            if(!json.get("parent_id").isJsonNull()) {
                parentId = json.get("parent_id").getAsString();
            }
        }

        Set<ChannelFlag> flags = new HashSet<>();
        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isJsonNull()) {
                flagsRaw = json.get("flags").getAsLong();
            }
        }

        for(ChannelFlag el: ChannelFlag.values()) {
            if((el.getCode() & flagsRaw) == el.getCode()) {
                flags.add(el);
            }
        }
        /////////////////////////////////////////////////////

        int msgCount = 0;
        if(json.get("message_count") != null) {
            if(!json.get("message_count").isJsonNull()) {
                msgCount = json.get("message_count").getAsInt();
            }
        }

        int memberCount = 0;
        if(json.get("member_count") != null) {
            if(!json.get("member_count").isJsonNull()) {
                memberCount = json.get("member_count").getAsInt();
            }
        }

        ThreadMetadata metadata = null;
        if(json.get("thread_metadata") != null) {
            if(!json.get("thread_metadata").isJsonNull()) {
                JsonObject o = json.get("thread_metadata").getAsJsonObject();
                metadata = ThreadMetadata.fromJson(guildId, o);
            }
        }

        int totalMsgSent = 0;
        if(json.get("total_message_sent") != null) {
            if(!json.get("total_message_sent").isJsonNull()) {
                totalMsgSent = json.get("total_message_sent").getAsInt();
            }
        }

        return new ThreadChannel(client, id, type, guildId, position, overwrites, name, nsfw, parentId, flags, memberCount, msgCount, metadata, totalMsgSent);
    }

    /**
     * @return Number of messages (not including the initial message or deleted messages) in a thread.**/
    public int getMessageCount() {
        return msgCount;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public ThreadMetadata getMetadata() {
        return metadata;
    }

    /**
     * @return Number of messages ever sent in a thread, it's similar to getMessageCount on message creation, but will not decrement the number when a message is deleted**/
    public int getTotalMessagesSent() {
        return totalMsgSent;
    }
}
