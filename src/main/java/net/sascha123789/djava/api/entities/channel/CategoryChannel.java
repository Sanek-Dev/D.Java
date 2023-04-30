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
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashSet;
import java.util.Set;

public class CategoryChannel extends BaseChannel {
    private CategoryChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
    }

    public Updater getUpdater() {
        return new Updater(client, this);
    }

    public static class Updater {
        private DiscordClient client;
        private CategoryChannel channel;
        private String name;
        private int position;
        private Set<PermissionOverwrite> overwrites;
        private Set<ChannelFlag> flags;

        public Updater(DiscordClient client, CategoryChannel channel) {
            this.client = client;
            this.channel = channel;
            this.name = "";
            this.position = -1;
            this.overwrites = new HashSet<>();
            this.flags = new HashSet<>();
        }

        public Updater setFlags(Set<ChannelFlag> flags) {
            this.flags = flags;
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

            if (!overwrites.isEmpty()) {
                JsonArray arr = new JsonArray();

                for (PermissionOverwrite overwrite : overwrites) {
                    JsonObject o = overwrite.toJson();
                    arr.add(o);
                }

                obj.add("permission_overwrites", arr);
            }

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

    public static CategoryChannel fromJson(DiscordClient client, JsonObject json) {
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

        return new CategoryChannel(client, id, type, guildId, position, overwrites, name, nsfw, parentId, flags);
    }
}
