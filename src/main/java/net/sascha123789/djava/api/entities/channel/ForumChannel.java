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
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashSet;
import java.util.Set;

public class ForumChannel extends BaseChannel {
    private Set<ForumTag> availableTags;
    private Set<ForumTag> appliedTags;
    private DefaultReaction defaultReaction;
    private ForumOrderType sortOrder;
    private ForumLayout layoutType;

    private ForumChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags, Set<ForumTag> availableTags, Set<ForumTag> appliedTags, DefaultReaction defaultReaction, ForumOrderType sortOrder, ForumLayout layoutType) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
        this.availableTags = availableTags;
        this.appliedTags = appliedTags;
        this.defaultReaction = defaultReaction;
        this.layoutType = layoutType;
        this.sortOrder = sortOrder;
    }

    public Updater getUpdater() {
        return new Updater(client, this);
    }

    public static class Updater {
        private DiscordClient client;
        private ForumChannel channel;
        private String name;
        private int position;
        private Set<PermissionOverwrite> overwrites;
        private String parentId;
        private Set<ChannelFlag> flags;
        private boolean nsfw;
        private int rateLimit;
        private ForumOrderType sortOrder;
        private ForumLayout layoutType;

        public Updater(DiscordClient client, ForumChannel channel) {
            this.client = client;
            this.channel = channel;
            this.name = "";
            this.position = -1;
            this.overwrites = new HashSet<>();
            this.parentId = "";
            this.rateLimit = -1;
            this.nsfw = false;
            this.flags = new HashSet<>();
            this.sortOrder = null;
            this.layoutType = null;
        }

        public Updater setLayoutType(ForumLayout layout) {
            this.layoutType = layout;
            return this;
        }

        public Updater setSortOrder(ForumOrderType sortOrder) {
            this.sortOrder = sortOrder;
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

            if (!overwrites.isEmpty()) {
                ArrayNode arr = Constants.MAPPER.createArrayNode();

                for (PermissionOverwrite overwrite : overwrites) {
                    arr.add(overwrite.toJson());
                }

                obj.set("permission_overwrites", arr);
            }

            if(layoutType != null) {
                obj.put("default_forum_layout", (layoutType == ForumLayout.NOT_SET ? 0 : (layoutType == ForumLayout.LIST_VIEW ? 1 : 2)));
            }

            if(sortOrder != null) {
                obj.put("default_sort_order", (sortOrder == ForumOrderType.LATEST_ACTIVITY ? 0 : 1));
            }

            obj.put("nsfw", nsfw);

            if (!parentId.isEmpty()) {
                obj.put("parent_id", parentId);
            }

            if(rateLimit != -1) {
                obj.put("rate_limit_per_user", Math.abs(rateLimit));
            }

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
        }
    }

    public static ForumChannel fromJson(DiscordClient client, JsonNode json) {
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

        Set<ForumTag> availableTags = new HashSet<>();
        if(json.get("available_tags") != null) {
            if(!json.get("available_tags").isNull()) {
                JsonNode arr = json.get("available_tags");

                for(JsonNode el: arr) {
                    availableTags.add(ForumTag.fromJson(el));
                }
            }
        }

        Set<ForumTag> appliedTags = new HashSet<>();
        if(json.get("applied_tags") != null) {
            if(!json.get("applied_tags").isNull()) {
                JsonNode arr = json.get("applied_tags");

                for(JsonNode el: arr) {
                    appliedTags.add(ForumTag.fromJson(el));
                }
            }
        }

        DefaultReaction defaultReaction = null;
        if(json.get("default_reaction_emoji") != null) {
            if(!json.get("default_reaction_emoji").isNull()) {
                defaultReaction = DefaultReaction.fromJson(json.get("default_reaction_emoji"));
            }
        }

        ForumOrderType sortOrder = ForumOrderType.LATEST_ACTIVITY;
        if(json.get("default_sort_order") != null) {
            if(!json.get("default_sort_order").isNull()) {
                int typ = json.get("default_sort_order").asInt();
                sortOrder = (typ == 0 ? ForumOrderType.LATEST_ACTIVITY : ForumOrderType.CREATION_DATE);
            }
        }

        ForumLayout layoutType = ForumLayout.NOT_SET;
        if(json.get("default_forum_layout") != null) {
            if(!json.get("default_forum_layout").isNull()) {
                int typ = json.get("default_forum_layout").asInt();
                layoutType = (typ == 0 ? ForumLayout.NOT_SET : (typ == 1 ? ForumLayout.LIST_VIEW : ForumLayout.GALLERY_VIEW));
            }
        }

        if(client.isOptimized()) {
            System.gc();
        }

        return new ForumChannel(client, id, type, guildId, position, overwrites, name, nsfw, parentId, flags, availableTags, appliedTags, defaultReaction, sortOrder, layoutType);
    }

    public ImmutableSet<ForumTag> getAvailableTags() {
        return ImmutableSet.copyOf(availableTags);
    }

    public ImmutableSet<ForumTag> getAppliedTags() {
        return ImmutableSet.copyOf(appliedTags);
    }

    public DefaultReaction getDefaultReaction() {
        return defaultReaction;
    }

    public ForumOrderType getSortOrder() {
        return sortOrder;
    }

    public ForumLayout getLayoutType() {
        return layoutType;
    }
}
