/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.slash.SubCommand;
import net.sascha123789.djava.api.interactions.slash.SubCommandGroup;
import net.sascha123789.djava.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SubCommandGroupSerializer implements JsonSerializer<SubCommandGroup> {
    @Override
    public JsonElement serialize(SubCommandGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.addProperty("description", src.getDescription());
        Map<String, JsonElement> nameLocals = new HashMap<>();

        if(!src.getNameLocalizations().isEmpty()) {
            for(Map.Entry<DiscordLanguage, String> entry: src.getNameLocalizations().entrySet()) {
                nameLocals.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }

            obj.add("name_localizations", Constants.GSON.toJsonTree(nameLocals));
        }

        Map<String, JsonElement> descLocals = new HashMap<>();

        if(!src.getDescriptionLocalizations().isEmpty()) {
            for(Map.Entry<DiscordLanguage, String> entry: src.getDescriptionLocalizations().entrySet()) {
                descLocals.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }

            obj.add("description_localizations", Constants.GSON.toJsonTree(descLocals));
        }
        obj.addProperty("type", 2);
        JsonArray arr = new JsonArray();

        if(!src.getSubCommands().isEmpty()) {
            for(SubCommand subCommand: src.getSubCommands()) {
                arr.add(Constants.GSON.toJsonTree(subCommand, SubCommand.class));
            }
        } else {
            // Default options handling
        }

        obj.add("options", arr);

        return obj;
    }
}
