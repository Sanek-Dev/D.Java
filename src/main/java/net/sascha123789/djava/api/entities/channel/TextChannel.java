/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;

import java.io.File;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class TextChannel extends MessageableChannel {
    private String topic;
    private String lastMsgId;
    private int rateLimit;
    private Timestamp lastPinTimestamp;
    private int archiveDuration;

    private TextChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags, String topic, String lastMsgId, int rateLimit, int archiveDuration, Timestamp lastPinTimestamp) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
        this.topic = topic;
        this.lastMsgId = lastMsgId;
        this.archiveDuration = archiveDuration;
        this.rateLimit = rateLimit;
        this.lastPinTimestamp = lastPinTimestamp;
    }

    public Optional<ThreadChannel> startThread(String name, ThreadType type, boolean invitable) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("type", (type == ThreadType.PRIVATE ? 12 : (type == ThreadType.PUBLIC ? 11 : 10)));
        obj.addProperty("invitable", invitable);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/threads")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadChannel.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ThreadChannel> startThread(String name, ThreadType type) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("type", (type == ThreadType.PRIVATE ? 12 : (type == ThreadType.PUBLIC ? 11 : 10)));

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/threads")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadChannel.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ThreadChannel> startThread(String name, boolean invitable) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);
        obj.addProperty("invitable", invitable);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/threads")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadChannel.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<ThreadChannel> startThread(String name) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/threads")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadChannel.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Updater getUpdater() {
        return new Updater(client, this);
    }

    public static class Updater {
        private DiscordClient client;
        private TextChannel channel;
        private String name;
        private String topic;
        private int position;
        private boolean nsfw;
        private int rateLimit;
        private Set<PermissionOverwrite> overwrites;
        private String parentId;
        private Set<ChannelFlag> flags;

        public Updater(DiscordClient client, TextChannel channel) {
            this.client = client;
            this.channel = channel;
            this.name = "";
            this.topic = "";
            this.position = -1;
            this.nsfw = false;
            this.rateLimit = -1;
            this.overwrites = new HashSet<>();
            this.parentId = "";
            this.flags = new HashSet<>();
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

        public Updater setRateLimitPerUser(int rateLimit) {
            this.rateLimit = rateLimit;
            return this;
        }

        public Updater setNsfw(boolean nsfw) {
            this.nsfw = nsfw;
            return this;
        }

        public Updater setPosition(int position) {
            this.position = position;
            return this;
        }

        public Updater setTopic(String topic) {
            this.topic = topic;
            return this;
        }

        public Updater setName(String name) {
            this.name = name;
            return this;
        }

        public void update() {
            JsonObject obj = new JsonObject();

            if(!name.isEmpty()) {
                obj.addProperty("name", name);
            }

            if(!topic.isEmpty()) {
                obj.addProperty("topic", topic);
            }

            if(position != -1) {
                obj.addProperty("position", Math.abs(position));
            }

            obj.addProperty("nsfw", nsfw);

            if(rateLimit != -1) {
                obj.addProperty("rate_limit_per_user", Math.abs(rateLimit));
            }

            if(!overwrites.isEmpty()) {
                JsonArray arr = new JsonArray();

                for(PermissionOverwrite overwrite: overwrites) {
                    JsonObject o = overwrite.toJson();
                    arr.add(o);
                }

                obj.add("permission_overwrites", arr);
            }

            if(!parentId.isEmpty()) {
                obj.addProperty("parent_id", parentId);
            }

            if(!flags.isEmpty()) {
                long flags = 0;
                for(ChannelFlag el: this.flags) {
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

    public static TextChannel fromJson(DiscordClient client, JsonObject json) {
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

        String topic = "";
        if(json.get("topic") != null) {
            if(!json.get("topic").isJsonNull()) {
                topic = json.get("topic").getAsString();
            }
        }

        String lastMsgId = "";
        if(json.get("last_message_id") != null) {
            if(!json.get("last_message_id").isJsonNull()) {
                lastMsgId = json.get("last_message_id").getAsString();
            }
        }

        int rateLimit = 0;
        if(json.get("rate_limit_per_user") != null) {
            if(!json.get("rate_limit_per_user").isJsonNull()) {
                rateLimit = json.get("rate_limit_per_user").getAsInt();
            }
        }

        int archiveDuration = 0;
        if(json.get("default_auto_archive_duration") != null) {
            if(!json.get("default_auto_archive_duration").isJsonNull()) {
                archiveDuration = json.get("default_auto_archive_duration").getAsInt();
            }
        }

        Timestamp lastPinTimestamp = null;
        if(json.get("last_pin_timestamp") != null) {
            if(!json.get("last_pin_timestamp").isJsonNull()) {
                String s = json.get("last_pin_timestamp").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                lastPinTimestamp = Timestamp.valueOf(s);
            }
        }

        return new TextChannel(client, id, type, guildId, position, overwrites, name, nsfw, parentId, flags, topic, lastMsgId, rateLimit, archiveDuration, lastPinTimestamp);
    }

    public String getTopic() {
        return topic;
    }

    public String getLastMsgId() {
        return lastMsgId;
    }

    /**
     * @return Amount of seconds a user has to wait before sending another message (0-21600); bots, as well as users with the permission manage_messages or manage_channel, are unaffected**/
    public int getRateLimitPerUser() {
        return rateLimit;
    }

    public Timestamp getLastPinTimestamp() {
        return lastPinTimestamp;
    }

    /**
     * @return Default duration, copied onto newly created threads, in minutes, threads will stop showing in the channel list after the specified period of inactivity**/
    public int getArchiveDuration() {
        return archiveDuration;
    }
}
