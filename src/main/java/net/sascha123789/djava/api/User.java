/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api;

import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.api.enums.NitroType;
import net.sascha123789.djava.api.enums.UserFlag;
import net.sascha123789.djava.utils.Constants;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;


public class User implements Identifiable {
    private String id;
    private String username;
    private String discriminator;
    private String avatar;
    private boolean bot;
    private boolean system;
    private boolean mfaEnabled;
    private String banner;
    private int accentColor;
    private String locale;
    private int flags;
    private int publicFlags;
    private int premiumType;

    public User(String id, String username, String discriminator, String avatar, boolean bot, boolean system, boolean mfaEnabled, String banner, int accentColor, String locale, int flags, int publicFlags, int premiumType) {
        this.id = id;
        this.username = username;
        this.discriminator = discriminator;
        this.avatar = avatar;
        this.bot = bot;
        this.system = system;
        this.mfaEnabled = mfaEnabled;
        this.banner = banner;
        this.accentColor = accentColor;
        this.locale = locale;
        this.flags = flags;
        this.publicFlags = publicFlags;
        this.premiumType = premiumType;
    }

    /**
     * @return The type of Nitro subscription on a user's account**/
    public NitroType getNitroType() {
        return (premiumType == 0 ? NitroType.NONE : (premiumType == 1 ? NitroType.CLASSIC : (premiumType == 2 ? NitroType.NITRO : NitroType.BASIC)));
    }

    /**
     * @return The type of Nitro subscription on a user's account**/
    public int getNitroTypeRaw() {
        return premiumType;
    }

    /**
     * @return The public flags on a user's account**/
    public List<UserFlag> getPublicFlags() {
        List<UserFlag> list = new ArrayList<>();

        if(publicFlags != 0) {
            for(UserFlag flag: UserFlag.values()) {
                if((flag.getCode() & publicFlags) == flag.getCode()) {
                    list.add(flag);
                }
            }
        }

        return list;
    }

    /**
     * @return The public flags on a user's account**/
    public int getPublicFlagsRaw() {
        return publicFlags;
    }

    /**
     * @return The flags on a user's account**/
    public List<UserFlag> getFlags() {
        List<UserFlag> list = new ArrayList<>();

        if(flags != 0) {
            for(UserFlag flag: UserFlag.values()) {
                if((flag.getCode() & flags) == flag.getCode()) {
                    list.add(flag);
                }
            }
        }

        return list;
    }

    /**
     * @return The flags on a user's account**/
    public int getFlagsRaw() {
        return flags;
    }

    /**
     * @return The user's chosen language option**/
    public DiscordLanguage getLocale() {
        DiscordLanguage loc = null;

        for(DiscordLanguage lang: DiscordLanguage.values()) {
            if(locale.equals(lang.getId())) {
                loc = lang;
                break;
            }
        }

        return loc;
    }

    @Override
    public String toString() {
        return username + "#" + discriminator;
    }

    /**
     * @return The user's chosen language option**/
    public String getLocaleRaw() {
        return locale;
    }

    /**
     * @return The user's banner color**/
    public Color getAccentColor() {
        if(accentColor == 0) return null;
        return Color.decode(String.valueOf(accentColor));
    }

    /**
     * @return The user's banner color encoded as an integer representation of hexadecimal color code**/
    public int getAccentColorRaw() {
        return accentColor;
    }

    /**
     * @return The user's banner hash**/
    public String getBannerHash() {
        return banner;
    }

    /**
     * @return User banner url
     * @apiNote Default image type is png**/
    public String getBannerUrl() {
        return Constants.BASE_IMAGES_URL + "banners/" + id + "/" + banner + ".png";
    }

    /**
     * @return User banner url**/
    public String getBannerUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "banners/" + id + "/" + banner + (type == ImageType.GIF ? ".gif" : (type == ImageType.JPEG ? ".jpg" : (type == ImageType.PNG ? ".png" : ".webp")));
    }

    /**
     * @return Whether the user has two factor enabled on their account**/
    public boolean isMfaEnabled() {
        return mfaEnabled;
    }

    /**
     * @return The user's username, not unique across the platform**/
    public String getUsername() {
        return username;
    }

    /**
     * @return The user's 4-digit discord-tag**/
    public String getDiscriminator() {
        return discriminator;
    }

    /**
     * @return The user's avatar hash**/
    public String getAvatarHash() {
        return avatar;
    }

    /**
     * @return The user's avatar url
     * @apiNote Default image type is png**/
    public String getAvatarUrl() {
        return Constants.BASE_IMAGES_URL + "avatars/" + id + "/" + avatar + ".png";
    }

    /**
     * @return The user's avatar url**/
    public String getAvatarUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "avatars/" + id + "/" + avatar + (type == ImageType.GIF ? ".gif" : (type == ImageType.JPEG ? ".jpg" : (type == ImageType.PNG ? ".png" : ".webp")));
    }

    /**
     * @return Whether the user belongs to an OAuth2 application**/
    public boolean isBot() {
        return bot;
    }

    /**
     * @return Whether the user is an Official Discord System user (part of the urgent message system)**/
    public boolean isSystem() {
        return system;
    }

    /**
     * @return The user's id**/
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return The user's id as Long**/
    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }
}
