/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlashCommandDeserializer implements JsonDeserializer<SlashCommand> {
    @Override
    public SlashCommand deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        String id = src.get("id").getAsString();
        String appId = src.get("application_id").getAsString();
        String guildId = "CmdIsGlobal";
        if(src.get("guild_id") != null) {
            if(!src.get("guild_id").isJsonNull()) {
                guildId = src.get("guild_id").getAsString();
            }
        }
        String name = src.get("name").getAsString();
        Map<DiscordLanguage, String> nameLocals = new HashMap<>();

        if(src.get("name_localizations") != null) {
            if(!src.get("name_localizations").isJsonNull()) {
                JsonObject o = src.get("name_localizations").getAsJsonObject();

                for(Map.Entry<String, JsonElement> entry: o.asMap().entrySet()) {
                    DiscordLanguage lang = null;

                    for(DiscordLanguage language: DiscordLanguage.values()) {
                        if(language.getId().equals(entry.getKey())) {
                            lang = language;
                            break;
                        }
                    }

                    nameLocals.put(lang, entry.getValue().getAsString());
                }
            }
        }

        Map<DiscordLanguage, String> descLocals = new HashMap<>();

        if(src.get("description_localizations") != null) {
            if(!src.get("description_localizations").isJsonNull()) {
                JsonObject o = src.get("description_localizations").getAsJsonObject();

                for(Map.Entry<String, JsonElement> entry: o.asMap().entrySet()) {
                    DiscordLanguage lang = null;

                    for(DiscordLanguage language: DiscordLanguage.values()) {
                        if(language.getId().equals(entry.getKey())) {
                            lang = language;
                            break;
                        }
                    }

                    descLocals.put(lang, entry.getValue().getAsString());
                }
            }
        }

        String description = src.get("description").getAsString();
        boolean dmPermission = true;
        if(src.get("dm_permission") != null) {
            if(!src.get("dm_permission").isJsonNull()) {
                dmPermission = src.get("dm_permission").getAsBoolean();
            }
        }
        boolean nsfw = false;
        if(src.get("nsfw") != null) {
            if(!src.get("nsfw").isJsonNull()) {
                nsfw = src.get("nsfw").getAsBoolean();
            }
        }
        boolean availableForEveryone = false;
        List<DiscordPermission> requiredPermissions = new ArrayList<>();
        if(src.get("default_member_permissions") == null) {
            availableForEveryone = true;
        } else {
            if(!src.get("default_member_permissions").isJsonNull()) {
                long bits = Long.parseLong(src.get("default_member_permissions").getAsString());

                for(DiscordPermission permission: DiscordPermission.values()) {
                    if((permission.getCode() & bits) == permission.getCode()) {
                        requiredPermissions.add(permission);
                    }
                }
            } else {
                availableForEveryone = true;
            }
        }

        return new SlashCommand(id, appId, guildId, name, nameLocals, descLocals, description, dmPermission, nsfw, requiredPermissions, availableForEveryone, null, null, null);
    }
}
