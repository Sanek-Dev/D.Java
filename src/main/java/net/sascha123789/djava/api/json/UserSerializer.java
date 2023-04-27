/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import net.sascha123789.djava.api.User;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User> {
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("id", src.getId());
        obj.addProperty("username", src.getUsername());
        obj.addProperty("discriminator", src.getDiscriminator());
        obj.addProperty("avatar", src.getAvatarHash());
        obj.addProperty("bot", src.isBot());
        obj.addProperty("system", src.isSystem());
        obj.addProperty("mfa_enabled", src.isMfaEnabled());
        obj.addProperty("banner", src.getBannerHash());
        obj.addProperty("accent_color", src.getAccentColorRaw());
        obj.addProperty("locale", src.getLocaleRaw());
        obj.addProperty("flags", src.getFlagsRaw());
        obj.addProperty("public_flags", src.getPublicFlagsRaw());
        obj.addProperty("premium_type", src.getNitroTypeRaw());

        return obj;
    }
}
