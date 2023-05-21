/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
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

    public final Optional<ImmutableSet<ThreadMember>> getThreadMembers() {
        HttpUrl.Builder url = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/thread-members").newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            JsonNode arr = Constants.MAPPER.readTree(res);
            Set<ThreadMember> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(ThreadMember.fromJson(client, el));
            }

            return Optional.of(ImmutableSet.copyOf(set));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public final Optional<ThreadMember> getMemberById(String userId) {
        HttpUrl.Builder url = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/thread-members/" + userId).newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        if(client.isOptimized()) {
            System.gc();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadMember.fromJson(client, Constants.MAPPER.readTree(res)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public final void removeMember(String userId) {
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

    public final void addMember(String userId) {
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

        public Updater setParent(CategoryChannel channel) {
            this.parentId = channel.getId();
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

        public final void update() {
            ObjectNode obj = Constants.MAPPER.createObjectNode();

            if (!name.isEmpty()) {
                obj.put("name", name);
            }

            if (position != -1) {
                obj.put("position", Math.abs(position));
            }

            if(archiveDuration != -1) {
                obj.put("auto_archive_duration", Math.abs(archiveDuration));
            }

            obj.put("locked", locked);

            if(invit == 0) {
                obj.put("invitable", invitable);
            }

            if(rateLimit != -1) {
                obj.put("rate_limit_per_user", Math.abs(rateLimit));
            }

            if(!appliedTags.isEmpty()) {
                ArrayNode arr = Constants.MAPPER.createArrayNode();

                for(String el: appliedTags) {
                    arr.add(el);
                }

                obj.set("applied_tags", arr);
            }

            if (!overwrites.isEmpty()) {
                ArrayNode arr = Constants.MAPPER.createArrayNode();

                for (PermissionOverwrite overwrite : overwrites) {
                    arr.add(overwrite.toJson());
                }

                obj.set("permission_overwrites", arr);
            }

            if (!parentId.isEmpty()) {
                obj.put("parent_id", parentId);
            }
            obj.put("archived", archived);

            if (!flags.isEmpty()) {
                long flags = 0;
                for (ChannelFlag el : this.flags) {
                    flags += el.getCode();
                }
                obj.put("flags", flags);
            }

            try {
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/channels/" + channel.getId())
                        .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json"))).build();

                try(Response resp = client.getHttpClient().newCall(request).execute()) {
                    String str = resp.body().string();

                    ErrHandler.handle(str);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }

            if(client.isOptimized()) {
                System.gc();
            }
        }
    }

    public static ThreadChannel fromJson(DiscordClient client, JsonNode json) {
        /* Base */
        String id = json.get("id").asText();
        int t = json.get("type").asInt();
        ChannelType type = (t == 0 ? ChannelType.TEXT : (t == 2 ? ChannelType.VOICE : (t == 4 ? ChannelType.CATEGORY : (t == 5 ? ChannelType.ANNOUNCEMENT : (t == 10 ? ChannelType.ANNOUNCEMENT : (t == 11 ? ChannelType.PUBLIC_THREAD : (t == 12 ? ChannelType.PRIVATE_THREAD : (t == 13 ? ChannelType.STAGE : (t == 14 ? ChannelType.DIRECTORY : ChannelType.FORUM)))))))));
        String guildId = "";

        if(json.get("guild_id") != null) {
            if(!json.get("guild_id").isNull()) {
                guildId = json.get("guild_id").asText();
            }
        }

        int position = 0;
        if(json.get("position") != null) {
            if(!json.get("position").isNull()) {
                position = json.get("position").asInt();
            }
        }

        Set<PermissionOverwrite> overwrites = new HashSet<>();

        if(json.get("permission_overwrites") != null) {
            if(!json.get("permission_overwrites").isNull()) {
                JsonNode arr = json.get("permission_overwrites");

                for(JsonNode el: arr) {
                    overwrites.add(PermissionOverwrite.fromJson(el));
                }
            }
        }

        String name = "";
        if(json.get("name") != null) {
            if(!json.get("name").isNull()) {
                name = json.get("name").asText();
            }
        }

        boolean nsfw = false;
        if(json.get("nsfw") != null) {
            if(!json.get("nsfw").isNull()) {
                nsfw = json.get("nsfw").asBoolean();
            }
        }

        String parentId = "";
        if(json.get("parent_id") != null) {
            if(!json.get("parent_id").isNull()) {
                parentId = json.get("parent_id").asText();
            }
        }

        Set<ChannelFlag> flags = new HashSet<>();
        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isNull()) {
                flagsRaw = json.get("flags").asLong();
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
            if(!json.get("message_count").isNull()) {
                msgCount = json.get("message_count").asInt();
            }
        }

        int memberCount = 0;
        if(json.get("member_count") != null) {
            if(!json.get("member_count").isNull()) {
                memberCount = json.get("member_count").asInt();
            }
        }

        ThreadMetadata metadata = null;
        if(json.get("thread_metadata") != null) {
            if(!json.get("thread_metadata").isNull()) {
                JsonNode o = json.get("thread_metadata");
                metadata = ThreadMetadata.fromJson(guildId, o);
            }
        }

        int totalMsgSent = 0;
        if(json.get("total_message_sent") != null) {
            if(!json.get("total_message_sent").isNull()) {
                totalMsgSent = json.get("total_message_sent").asInt();
            }
        }

        if(client.isOptimized()) {
            System.gc();
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
