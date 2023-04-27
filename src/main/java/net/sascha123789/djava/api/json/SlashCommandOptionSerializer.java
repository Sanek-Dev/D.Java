/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.OptionChoice;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.utils.Constants;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandOptionSerializer implements JsonSerializer<SlashCommandOption> {
    @Override
    public JsonElement serialize(SlashCommandOption src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject obj = new JsonObject();
        obj.addProperty("type", (src.getType() == SlashCommandOptionType.ATTACHMENT ? 11 : (src.getType() == SlashCommandOptionType.BOOLEAN ? 5 : (src.getType() == SlashCommandOptionType.CHANNEL ? 7 : (src.getType() == SlashCommandOptionType.INTEGER ? 4 : (src.getType() == SlashCommandOptionType.MENTIONABLE ? 9 : (src.getType() == SlashCommandOptionType.NUMBER ? 10 : (src.getType() == SlashCommandOptionType.ROLE ? 8 : (src.getType() == SlashCommandOptionType.STRING ? 3 : 6)))))))));
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

        obj.addProperty("required", src.isRequired());

        if(src.getType() == SlashCommandOptionType.STRING && !src.getChoices().isEmpty()) {
            JsonArray arr = new JsonArray();

            for(OptionChoice choice: src.getChoices()) {
                JsonObject object = new JsonObject();
                object.addProperty("name", choice.getName());
                object.addProperty("value", choice.getValue());

                if(!choice.getNameLocalizations().isEmpty()) {
                    Map<String, JsonElement> map = new HashMap<>();

                    for(Map.Entry<DiscordLanguage, String> entry: choice.getNameLocalizations().entrySet()) {
                        map.put(entry.getKey().getId(), new JsonPrimitive(entry.getValue()));
                    }

                    object.add("name_localizations", Constants.GSON.toJsonTree(map));
                }

                arr.add(object);
            }

            obj.add("choices", arr);
        }

        if(!src.getChannelTypes().isEmpty()) {
            JsonArray types = new JsonArray();

            for(ChannelType type: src.getChannelTypes()) {
                types.add((type == ChannelType.ANNOUNCEMENT ? 5 : (type == ChannelType.DM ? 1 : (type == ChannelType.DIRECTORY ? 14 : (type == ChannelType.CATEGORY ? 4 : (type == ChannelType.FORUM ? 15 : (type == ChannelType.GROUP_DM ? 3 : (type == ChannelType.PRIVATE_THREAD ? 12 : (type == ChannelType.PUBLIC_THREAD ? 11 : (type == ChannelType.STAGE ? 13 : (type == ChannelType.TEXT ? 0 : 2)))))))))));
            }

            obj.add("channel_types", types);
        }

        if(src.getType() == SlashCommandOptionType.INTEGER || src.getType() == SlashCommandOptionType.NUMBER) {
            if(src.getMinValue() != 0 && src.getMaxValue() != 0) {
                obj.addProperty("min_value", src.getMinValue());
                obj.addProperty("max_value", src.getMaxValue());
            }
        }

        if(src.getType() == SlashCommandOptionType.STRING) {
            if(src.getMinLength() != 0 && src.getMaxLength() != 0) {
                obj.addProperty("min_length", src.getMinLength());
                obj.addProperty("max_length", src.getMaxLength());
            }
        }

        return obj;
    }
}
