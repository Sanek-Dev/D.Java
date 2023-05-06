/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.OptionChoice;
import net.sascha123789.djava.api.interactions.slash.SlashCommandOption;
import net.sascha123789.djava.utils.Constants;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SlashCommandOptionTypeAdapter extends TypeAdapter<SlashCommandOption> {
    @Override
    public void write(JsonWriter out, SlashCommandOption src) throws IOException {
        out.beginObject();

        out.name("type").value((src.getType() == SlashCommandOptionType.ATTACHMENT ? 11 : (src.getType() == SlashCommandOptionType.BOOLEAN ? 5 : (src.getType() == SlashCommandOptionType.CHANNEL ? 7 : (src.getType() == SlashCommandOptionType.INTEGER ? 4 : (src.getType() == SlashCommandOptionType.MENTIONABLE ? 9 : (src.getType() == SlashCommandOptionType.NUMBER ? 10 : (src.getType() == SlashCommandOptionType.ROLE ? 8 : (src.getType() == SlashCommandOptionType.STRING ? 3 : 6)))))))));
        out.name("name").value(src.getName());
        out.name("description").value(src.getDescription());

        if(!src.getNameLocalizations().isEmpty()) {
            out.name("name_localizations");
            out.beginObject();

            for(Map.Entry<DiscordLanguage, String> entry: src.getNameLocalizations().entrySet()) {
                out.name(entry.getKey().getId()).value(entry.getValue());
            }

            out.endObject();
        }

        if(!src.getDescriptionLocalizations().isEmpty()) {
            out.name("description_localizations");
            out.beginObject();

            for(Map.Entry<DiscordLanguage, String> entry: src.getDescriptionLocalizations().entrySet()) {
                out.name(entry.getKey().getId()).value(entry.getValue());
            }

            out.endObject();
        }

        out.name("required").value(src.isRequired());

        if(src.getType() == SlashCommandOptionType.STRING && !src.getChoices().isEmpty()) {
            out.name("choices");
            out.beginArray();

            for(OptionChoice choice: src.getChoices()) {
                out.beginObject();
                out.name("name").value(choice.getName());
                out.name("value").value(choice.getValue());

                if(!choice.getNameLocalizations().isEmpty()) {
                    out.name("name_localizations");
                    out.beginObject();

                    for(Map.Entry<DiscordLanguage, String> entry: choice.getNameLocalizations().entrySet()) {
                        out.name(entry.getKey().getId()).value(entry.getValue());
                    }

                    out.endObject();
                }

                out.endObject();
            }

            out.endArray();
        }

        if(!src.getChannelTypes().isEmpty()) {
            out.name("channel_types");
            out.beginArray();

            for(ChannelType type: src.getChannelTypes()) {
                out.value((type == ChannelType.ANNOUNCEMENT ? 5 : (type == ChannelType.DM ? 1 : (type == ChannelType.DIRECTORY ? 14 : (type == ChannelType.CATEGORY ? 4 : (type == ChannelType.FORUM ? 15 : (type == ChannelType.GROUP_DM ? 3 : (type == ChannelType.PRIVATE_THREAD ? 12 : (type == ChannelType.PUBLIC_THREAD ? 11 : (type == ChannelType.STAGE ? 13 : (type == ChannelType.TEXT ? 0 : 2)))))))))));
            }

           out.endArray();
        }

        if(src.getType() == SlashCommandOptionType.INTEGER || src.getType() == SlashCommandOptionType.NUMBER) {
            if(src.getMinValue() != 0 && src.getMaxValue() != 0) {
                out.name("min_value").value(src.getMinValue());
                out.name("max_value").value(src.getMaxValue());
            }
        }

        if(src.getType() == SlashCommandOptionType.STRING) {
            if(src.getMinLength() != 0 && src.getMaxLength() != 0) {
                out.name("min_length").value(src.getMinLength());
                out.name("max_length").value(src.getMaxLength());
            }
        }

        out.endObject();
    }

    @Override
    public SlashCommandOption read(JsonReader in) throws IOException {
        return null;
    }
}
