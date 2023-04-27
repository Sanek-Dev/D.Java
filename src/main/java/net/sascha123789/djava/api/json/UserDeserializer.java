/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.User;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        String id = src.get("id").getAsString();
        String name = src.get("username").getAsString();
        String discriminator = src.get("discriminator").getAsString();
        String avatar = null;

        if(src.get("avatar") != null){
            if(!src.get("avatar").isJsonNull()) {
                avatar = src.get("avatar").getAsString();
            }
        }

        boolean bot = false;
        if(src.get("bot") != null) {
            if(!src.get("bot").isJsonNull()) {
                bot = src.get("bot").getAsBoolean();
            }
        }
        boolean system = false;
        if(src.get("system") != null) {
            if(!src.get("system").isJsonNull()) {
                system = src.get("system").getAsBoolean();
            }
        }
        boolean mfaEnabled = false;
        if(src.get("mfa_enabled") != null) {
            if(!src.get("mfa_enabled").isJsonNull()) {
                mfaEnabled = src.get("mfa_enabled").getAsBoolean();
            }
        }
        String banner = "";
        if(src.get("banner") != null) {
            if(!src.get("banner").isJsonNull()) {
                banner = src.get("banner").getAsString();
            }
        }
        int accentColor = 0;
        if(src.get("accent_color") != null) {
            if(!src.get("accent_color").isJsonNull()) {
                accentColor = src.get("accent_color").getAsInt();
            }
        }
        String locale = "";
        if(src.get("locale") != null) {
            if(!src.get("locale").isJsonNull()) {
                locale = src.get("locale").getAsString();
            }
        }
        int flags = 0;
        if(src.get("flags") != null) {
            if(!src.get("flags").isJsonNull()) {
                flags = src.get("flags").getAsInt();
            }
        }
        int publicFlags = 0;
        if(src.get("public_flags") != null) {
            if(!src.get("public_flags").isJsonNull()) {
                publicFlags = src.get("public_flags").getAsInt();
            }
        }
        int nitroType = 0;
        if(src.get("premium_type") != null) {
            if(!src.get("premium_type").isJsonNull()) {
                nitroType = src.get("premium_type").getAsInt();
            }
        }

        return new User(id, name, discriminator, avatar, bot, system, mfaEnabled, banner, accentColor, locale, flags, publicFlags, nitroType);
    }
}
