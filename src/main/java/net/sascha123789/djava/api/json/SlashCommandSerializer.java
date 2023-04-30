/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.api.interactions.slash.SubCommand;
import net.sascha123789.djava.api.interactions.slash.SubCommandGroup;
import net.sascha123789.djava.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandSerializer implements JsonSerializer<SlashCommand> {
    @Override
    public JsonElement serialize(SlashCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject object = new JsonObject();
        object.addProperty("name", src.getName());

        if(!src.getNameLocalizations().isEmpty()) {
            Map<String, JsonElement> map = new HashMap<>();

            for(Map.Entry<DiscordLanguage, String> entry: src.getNameLocalizations().entrySet()) {
                map.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }

            object.add("name_localizations", Constants.GSON.toJsonTree(map));
        }

        if(!src.getDescriptionLocalizations().isEmpty()) {
            Map<String, JsonElement> map = new HashMap<>();

            for(Map.Entry<DiscordLanguage, String> entry: src.getDescriptionLocalizations().entrySet()) {
                map.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }

            object.add("description_localizations", Constants.GSON.toJsonTree(map));
        }
        object.addProperty("description", src.getDescription());
        object.addProperty("dm_permission", src.isDmPermission());
        object.addProperty("nsfw", src.isNsfw());
        object.addProperty("type", 1);
        if(!src.isAvailableForEveryone() && !src.getRequiredPermissions().isEmpty()) {
            long bits = 0;

            for(DiscordPermission permission: src.getRequiredPermissions()) {
                bits += permission.getCode();
            }

            object.addProperty("default_member_permissions", String.valueOf(bits));
        } else {
            object.addProperty("default_member_permissions", String.valueOf(DiscordPermission.VIEW_CHANNEL.getCode()));
        }
        JsonArray arr = new JsonArray();

        if(!src.getSubcommandGroups().isEmpty()) {
            for(SubCommandGroup group: src.getSubcommandGroups()) {
                arr.add(Constants.GSON.toJsonTree(group, SubCommandGroup.class));
            }

            object.add("options", arr);
        } else {
            if(!src.getSubCommands().isEmpty()) {
                for(SubCommand el: src.getSubCommands()) {
                    arr.add(Constants.GSON.toJsonTree(el, SubCommand.class));
                }

                object.add("options", arr);
            } else {
                if(!src.getOptions().isEmpty()) {

                    for(SlashCommandOption option: src.getOptions()) {
                        arr.add(Constants.GSON.toJsonTree(option, SlashCommandOption.class));
                    }

                    object.add("options", arr);
                }
            }
        }

        return object;
    }
}
