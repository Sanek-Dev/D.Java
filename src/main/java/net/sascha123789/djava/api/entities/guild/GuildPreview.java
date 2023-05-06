/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Sticker;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class GuildPreview implements Identifiable {
    private String id;
    private String name;
    private String icon;
    private String splash;
    private String discoverySplash;
    private Set<Emoji> emojis;
    private Set<GuildFeature> features;
    private int memberCount;
    private int presenceCount;
    private String description;
    private Set<Sticker> stickers;
    private DiscordClient client;

    private GuildPreview(DiscordClient client, String id, String name, String icon, String splash, String discoverySplash, Set<Emoji> emojis, Set<GuildFeature> features, int memberCount, int presenceCount, String description, Set<Sticker> stickers) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.splash = splash;
        this.discoverySplash = discoverySplash;
        this.emojis = emojis;
        this.features = features;
        this.memberCount = memberCount;
        this.presenceCount = presenceCount;
        this.description = description;
        this.stickers = stickers;
        this.client = client;
    }

    public static GuildPreview fromJson(DiscordClient client, JsonNode json) {
        String id = json.get("id").asText();
        String name = json.get("name").asText();
        String icon = "";
        if(json.get("icon") != null) {
            if(!json.get("icon").isNull()) {
                icon = json.get("icon").asText();
            }
        }

        String splash = "";
        if(json.get("splash") != null) {
            if(!json.get("splash").isNull()) {
                splash = json.get("splash").asText();
            }
        }

        String discoverySplash = "";
        if(json.get("discovery_splash") != null) {
            if(!json.get("discovery_splash").isNull()) {
                discoverySplash = json.get("discovery_splash").asText();
            }
        }

        Set<Emoji> emojis = new HashSet<>();
        JsonNode emojisRaw = json.get("emojis");

        for(JsonNode el: emojisRaw) {
            emojis.add(Emoji.fromJson(client, el));
        }

        Set<GuildFeature> features = new HashSet<>();
        for(GuildFeature feature: GuildFeature.values()) {
            for(JsonNode el: json.get("features")) {
                if(feature.name().equals(el.asText())) {
                    features.add(feature);
                }
            }
        }

        int memberCount = 0;
        if(json.get("approximate_member_count") != null) {
            if(!json.get("approximate_member_count").isNull()) {
                memberCount = json.get("approximate_member_count").asInt();
            }
        }

        int presenceCount = 0;
        if(json.get("approximate_presence_count") != null) {
            if(!json.get("approximate_presence_count").isNull()) {
                presenceCount = json.get("approximate_presence_count").asInt();
            }
        }

        String description = "";
        if(json.get("description") != null) {
            if(!json.get("description").isNull()) {
                description = json.get("description").asText();
            }
        }

        Set<Sticker> stickers = new HashSet<>();
        JsonNode stickersRaw = json.get("stickers");

        for(JsonNode el: stickersRaw) {
            stickers.add(Sticker.fromJson(el));
        }

        return new GuildPreview(client, id, name, icon, splash, discoverySplash, emojis, features, memberCount, presenceCount, description, stickers);
    }

    public String getDiscoverySplashUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "discovery-splashes/" + id + "/" + discoverySplash + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    /**
     * @apiNote Default image type is png**/
    public String getDiscoverySplashUrl() {
        return Constants.BASE_IMAGES_URL + "discovery-splashes/" + id + "/" + discoverySplash + ".png";
    }

    public String getDiscoverySplashHash() {
        return discoverySplash;
    }

    /**
     * @apiNote Default image type is png**/
    public String getSplashUrl() {
        return Constants.BASE_IMAGES_URL + "splashes/" + id + "/" + splash + ".png";
    }

    public String getSplashUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "splashes/" + id + "/" + splash + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    public String getSplashHash() {
        return splash;
    }

    /**
     * @apiNote Default image type is png**/
    public String getIconUrl() {
        return Constants.BASE_IMAGES_URL + "icons/" + id + "/" + icon + ".png";
    }

    public String getIconUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "icons/" + id + "/" + icon + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    public String getIconHash() {
        return icon;
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

    public ImmutableSet<Emoji> getEmojis() {
        return ImmutableSet.copyOf(emojis);
    }

    public ImmutableSet<GuildFeature> getFeatures() {
        return ImmutableSet.copyOf(features);
    }

    public int getMemberCount() {
        return memberCount;
    }

    public int getPresenceCount() {
        return presenceCount;
    }

    public String getDescription() {
        return description;
    }

    public ImmutableSet<Sticker> getStickers() {
        return ImmutableSet.copyOf(stickers);
    }
}
