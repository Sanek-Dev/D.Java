/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.role;

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

    public Set<DiscordPermission> getPermissions() {
        Set<DiscordPermission> set = new HashSet<>();

        for(DiscordPermission perm: DiscordPermission.values()) {
            if((perm.getCode() & Long.parseLong(permissions)) == perm.getCode()) {
                set.add(perm);
            }
        }

        return set;
    }

    public boolean isIntegrationManaged() {
        return integrationManaged;
    }

    public boolean isMentionable() {
        return mentionable;
    }

    public static Role fromJson(DiscordClient client, JsonObject json) {
        String id = json.get("id").getAsString();
        String name = json.get("name").getAsString();
        Color color = Color.decode(String.valueOf(json.get("color").getAsInt()));
        boolean hoist = json.get("hoist").getAsBoolean();
        String icon = "";
        if(json.get("icon") != null) {
            if(!json.get("icon").isJsonNull()) {
                icon = json.get("icon").getAsString();
            }
        }
        String emoji = "";
        if(json.get("unicode_emoji") != null) {
            if(!json.get("unicode_emoji").isJsonNull()) {
                emoji = json.get("unicode_emoji").getAsString();
            }
        }
        int position = json.get("position").getAsInt();
        String permissions = json.get("permissions").getAsString();
        boolean managed = json.get("managed").getAsBoolean();
        boolean mentionable = json.get("mentionable").getAsBoolean();
        RoleTags tags = null;
        if(json.get("tags") != null) {
            if(!json.get("tags").isJsonNull()) {
                tags = RoleTags.fromJson(json.get("tags").getAsJsonObject());
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
