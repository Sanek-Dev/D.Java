/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.role;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;

import java.awt.*;
import java.util.HashSet;
import java.util.Set;

public class Role implements Identifiable {
    private DiscordClient client;
    private RoleTags tags;
    private String id;
    private String name;
    private Color color;
    private boolean hoist;
    private String iconHash;
    private String unicodeEmoji;
    private int position;
    private String permissions;
    private boolean integrationManaged;
    private boolean mentionable;

    private Role(DiscordClient client, RoleTags tags, String id, String name, Color color, boolean hoist, String iconHash, String unicodeEmoji, int position, String permissions, boolean integrationManaged, boolean mentionable) {
        this.client = client;
        this.tags = tags;
        this.id = id;
        this.name = name;
        this.color = color;
        this.hoist = hoist;
        this.iconHash = iconHash;
        this.unicodeEmoji = unicodeEmoji;
        this.position = position;
        this.permissions = permissions;
        this.integrationManaged = integrationManaged;
        this.mentionable = mentionable;
    }

    public String toMention() {
        return "<@&" + id + ">";
    }

    public RoleTags getTags() {
        return tags;
    }

    public String getName() {
        return name;
    }

    public Color getColor() {
        return color;
    }

    public boolean isHoist() {
        return hoist;
    }

    public String getIconHash() {
        return iconHash;
    }

    /**
     * @apiNote Default image type is png**/
    public String getIconUrl() {
        return Constants.BASE_IMAGES_URL + "role-icons/" + id + "/" + iconHash + ".png";
    }

    public String getIconUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "role-icons/" + id + "/" + iconHash + (type == ImageType.GIF ? ".gif" : (type == ImageType.JPEG ? ".jpg" : (type == ImageType.PNG ? ".png" : ".webp")));
    }

    public String getUnicodeEmoji() {
        return unicodeEmoji;
    }

    public int getPosition() {
        return position;
    }

    public String getPermissionsBitfield() {
        return permissions;
    }

    public final ImmutableSet<DiscordPermission> getPermissions() {
        Set<DiscordPermission> set = new HashSet<>();

        for(DiscordPermission perm: DiscordPermission.values()) {
            if((perm.getCode() & Long.parseLong(permissions)) == perm.getCode()) {
                set.add(perm);
            }
        }

        return ImmutableSet.copyOf(set);
    }

    public boolean isIntegrationManaged() {
        return integrationManaged;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public static Role fromJson(DiscordClient client, JsonNode json) {
        String id = json.get("id").asText();
        String name = json.get("name").asText();
        Color color = Color.decode(String.valueOf(json.get("color").asInt()));
        boolean hoist = json.get("hoist").asBoolean();
        String icon = "";
        if(json.get("icon") != null) {
            if(!json.get("icon").isNull()) {
                icon = json.get("icon").asText();
            }
        }
        String emoji = "";
        if(json.get("unicode_emoji") != null) {
            if(!json.get("unicode_emoji").isNull()) {
                emoji = json.get("unicode_emoji").asText();
            }
        }
        int position = json.get("position").asInt();
        String permissions = json.get("permissions").asText();
        boolean managed = json.get("managed").asBoolean();
        boolean mentionable = json.get("mentionable").asBoolean();
        RoleTags tags = null;
        if(json.get("tags") != null) {
            if(!json.get("tags").isNull()) {
                tags = RoleTags.fromJson(json.get("tags"));
            }
        }

        return new Role(client, tags, id, name, color, hoist, icon, emoji, position, permissions, managed, mentionable);
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
