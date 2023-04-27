/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.api.interactions.slash.SubCommand;
import net.sascha123789.djava.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SubCommandSerializer implements JsonSerializer<SubCommand> {
    @Override
    public JsonElement serialize(SubCommand src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", src.getName());
        obj.addProperty("description", src.getDescription());

        if(!src.getNameLocalizations().isEmpty()) {
            Map<String, JsonElement> nameLocals = new HashMap<>();
            for(Map.Entry<DiscordLanguage, String> entry: src.getNameLocalizations().entrySet()) {
                nameLocals.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }
            obj.add("name_localizations", Constants.GSON.toJsonTree(nameLocals));
        }

        if(!src.getDescriptionLocalizations().isEmpty()) {
            Map<String, JsonElement> descLocals = new HashMap<>();

            for(Map.Entry<DiscordLanguage, String> entry: src.getDescriptionLocalizations().entrySet()) {
                descLocals.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
            }

            obj.add("description_localizations", Constants.GSON.toJsonTree(descLocals));
        }

        obj.addProperty("type", 1);

        if(!src.getOptions().isEmpty()) {
            JsonArray arr = new JsonArray();

            for(SlashCommandOption option: src.getOptions()) {
                arr.add(Constants.GSON.toJsonTree(option, SlashCommandOption.class));
            }

            obj.add("options", arr);
        }

        return obj;
    }
}
