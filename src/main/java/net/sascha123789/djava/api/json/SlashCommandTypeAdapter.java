/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.JsonArray;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.*;
import net.sascha123789.djava.utils.Constants;

import java.io.IOException;
import java.util.Map;

public class SlashCommandTypeAdapter extends TypeAdapter<SlashCommand> {
    @Override
    public void write(JsonWriter out, SlashCommand element) throws IOException {
        out.beginObject();

        out.name("name").value(element.getName());

        if(!element.getNameLocalizations().isEmpty()) {
            out.name("name_localizations");
            out.beginObject();

            for(Map.Entry<DiscordLanguage, String> entry: element.getNameLocalizations().entrySet()) {
                out.name(entry.getKey().getId()).value(entry.getValue());
            }

            out.endObject();
        }

        if(!element.getDescriptionLocalizations().isEmpty()) {
            out.name("description_localizations");
            out.beginObject();

            for(Map.Entry<DiscordLanguage, String> entry: element.getDescriptionLocalizations().entrySet()) {
                out.name(entry.getKey().getId()).value(entry.getValue());
            }

            out.endObject();
        }

        out.name("description").value(element.getDescription());
        out.name("dm_permission").value(element.isDmPermission());
        out.name("nsfw").value(element.isNsfw());
        out.name("type").value(1);

        if(!element.isAvailableForEveryone() && !element.getRequiredPermissions().isEmpty()) {
            long bits = 0;

            for(DiscordPermission permission: element.getRequiredPermissions()) {
                bits += permission.getCode();
            }

            out.name("default_member_permissions").value(String.valueOf(bits));
        } else {
            out.name("default_member_permissions").value(String.valueOf(DiscordPermission.VIEW_CHANNEL.getCode()));
        }

        if(!element.getSubcommandGroups().isEmpty()) {
            out.name("options");
            out.beginArray();

            for(SubCommandGroup src: element.getSubcommandGroups()) {
                {
                    out.beginObject();
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
                    out.name("type").value(2);

                    if(!src.getSubCommands().isEmpty()) {
                        out.name("options");
                        out.beginArray();

                        for(SubCommand el: src.getSubCommands()) {
                            out.beginObject();
                            out.name("name").value(el.getName());
                            out.name("description").value(el.getDescription());

                            if(!el.getNameLocalizations().isEmpty()) {
                                out.name("name_localizations");
                                out.beginObject();

                                for(Map.Entry<DiscordLanguage, String> entry: el.getNameLocalizations().entrySet()) {
                                    out.name(entry.getKey().getId()).value(entry.getValue());
                                }

                                out.endObject();
                            }

                            if(!el.getDescriptionLocalizations().isEmpty()) {
                                out.name("description_localizations");
                                out.beginObject();

                                for(Map.Entry<DiscordLanguage, String> entry: el.getDescriptionLocalizations().entrySet()) {
                                    out.name(entry.getKey().getId()).value(entry.getValue());
                                }

                                out.endObject();
                            }

                            out.name("type").value(1);

                            if(!el.getOptions().isEmpty()) {
                                out.name("options");
                                out.beginArray();

                                for(SlashCommandOption option: el.getOptions()) {
                                    {
                                        out.beginObject();

                                        out.name("type").value((option.getType() == SlashCommandOptionType.ATTACHMENT ? 11 : (option.getType() == SlashCommandOptionType.BOOLEAN ? 5 : (option.getType() == SlashCommandOptionType.CHANNEL ? 7 : (option.getType() == SlashCommandOptionType.INTEGER ? 4 : (option.getType() == SlashCommandOptionType.MENTIONABLE ? 9 : (option.getType() == SlashCommandOptionType.NUMBER ? 10 : (option.getType() == SlashCommandOptionType.ROLE ? 8 : (option.getType() == SlashCommandOptionType.STRING ? 3 : 6)))))))));
                                        out.name("name").value(option.getName());
                                        out.name("description").value(option.getDescription());

                                        if(!option.getNameLocalizations().isEmpty()) {
                                            out.name("name_localizations");
                                            out.beginObject();

                                            for(Map.Entry<DiscordLanguage, String> entry: option.getNameLocalizations().entrySet()) {
                                                out.name(entry.getKey().getId()).value(entry.getValue());
                                            }

                                            out.endObject();
                                        }

                                        if(!option.getDescriptionLocalizations().isEmpty()) {
                                            out.name("description_localizations");
                                            out.beginObject();

                                            for(Map.Entry<DiscordLanguage, String> entry: option.getDescriptionLocalizations().entrySet()) {
                                                out.name(entry.getKey().getId()).value(entry.getValue());
                                            }

                                            out.endObject();
                                        }

                                        out.name("required").value(option.isRequired());

                                        if(option.getType() == SlashCommandOptionType.STRING && !option.getChoices().isEmpty()) {
                                            out.name("choices");
                                            out.beginArray();

                                            for(OptionChoice choice: option.getChoices()) {
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

                                        if(!option.getChannelTypes().isEmpty()) {
                                            out.name("channel_types");
                                            out.beginArray();

                                            for(ChannelType type: option.getChannelTypes()) {
                                                out.value((type == ChannelType.ANNOUNCEMENT ? 5 : (type == ChannelType.DM ? 1 : (type == ChannelType.DIRECTORY ? 14 : (type == ChannelType.CATEGORY ? 4 : (type == ChannelType.FORUM ? 15 : (type == ChannelType.GROUP_DM ? 3 : (type == ChannelType.PRIVATE_THREAD ? 12 : (type == ChannelType.PUBLIC_THREAD ? 11 : (type == ChannelType.STAGE ? 13 : (type == ChannelType.TEXT ? 0 : 2)))))))))));
                                            }

                                            out.endArray();
                                        }

                                        if(option.getType() == SlashCommandOptionType.INTEGER || option.getType() == SlashCommandOptionType.NUMBER) {
                                            if(option.getMinValue() != 0 && option.getMaxValue() != 0) {
                                                out.name("min_value").value(option.getMinValue());
                                                out.name("max_value").value(option.getMaxValue());
                                            }
                                        }

                                        if(option.getType() == SlashCommandOptionType.STRING) {
                                            if(option.getMinLength() != 0 && option.getMaxLength() != 0) {
                                                out.name("min_length").value(option.getMinLength());
                                                out.name("max_length").value(option.getMaxLength());
                                            }
                                        }

                                        out.endObject();
                                    }
                                }

                                out.endArray();
                            }

                            out.endObject();
                        }

                        out.endArray();
                    }

                    out.endObject();
                }
            }

            out.endArray();
        } else {
            if(!element.getSubCommands().isEmpty()) {
                out.name("options");
                out.beginArray();

                for(SubCommand src: element.getSubCommands()) {
                    {
                        out.beginObject();
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

                        out.name("type").value(1);

                        if(!src.getOptions().isEmpty()) {
                            out.name("options");
                            out.beginArray();

                            for(SlashCommandOption option: src.getOptions()) {
                                {
                                    out.beginObject();

                                    out.name("type").value((option.getType() == SlashCommandOptionType.ATTACHMENT ? 11 : (option.getType() == SlashCommandOptionType.BOOLEAN ? 5 : (option.getType() == SlashCommandOptionType.CHANNEL ? 7 : (option.getType() == SlashCommandOptionType.INTEGER ? 4 : (option.getType() == SlashCommandOptionType.MENTIONABLE ? 9 : (option.getType() == SlashCommandOptionType.NUMBER ? 10 : (option.getType() == SlashCommandOptionType.ROLE ? 8 : (option.getType() == SlashCommandOptionType.STRING ? 3 : 6)))))))));
                                    out.name("name").value(option.getName());
                                    out.name("description").value(option.getDescription());

                                    if(!option.getNameLocalizations().isEmpty()) {
                                        out.name("name_localizations");
                                        out.beginObject();

                                        for(Map.Entry<DiscordLanguage, String> entry: option.getNameLocalizations().entrySet()) {
                                            out.name(entry.getKey().getId()).value(entry.getValue());
                                        }

                                        out.endObject();
                                    }

                                    if(!option.getDescriptionLocalizations().isEmpty()) {
                                        out.name("description_localizations");
                                        out.beginObject();

                                        for(Map.Entry<DiscordLanguage, String> entry: option.getDescriptionLocalizations().entrySet()) {
                                            out.name(entry.getKey().getId()).value(entry.getValue());
                                        }

                                        out.endObject();
                                    }

                                    out.name("required").value(option.isRequired());

                                    if(option.getType() == SlashCommandOptionType.STRING && !option.getChoices().isEmpty()) {
                                        out.name("choices");
                                        out.beginArray();

                                        for(OptionChoice choice: option.getChoices()) {
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

                                    if(!option.getChannelTypes().isEmpty()) {
                                        out.name("channel_types");
                                        out.beginArray();

                                        for(ChannelType type: option.getChannelTypes()) {
                                            out.value((type == ChannelType.ANNOUNCEMENT ? 5 : (type == ChannelType.DM ? 1 : (type == ChannelType.DIRECTORY ? 14 : (type == ChannelType.CATEGORY ? 4 : (type == ChannelType.FORUM ? 15 : (type == ChannelType.GROUP_DM ? 3 : (type == ChannelType.PRIVATE_THREAD ? 12 : (type == ChannelType.PUBLIC_THREAD ? 11 : (type == ChannelType.STAGE ? 13 : (type == ChannelType.TEXT ? 0 : 2)))))))))));
                                        }

                                        out.endArray();
                                    }

                                    if(option.getType() == SlashCommandOptionType.INTEGER || option.getType() == SlashCommandOptionType.NUMBER) {
                                        if(option.getMinValue() != 0 && option.getMaxValue() != 0) {
                                            out.name("min_value").value(option.getMinValue());
                                            out.name("max_value").value(option.getMaxValue());
                                        }
                                    }

                                    if(option.getType() == SlashCommandOptionType.STRING) {
                                        if(option.getMinLength() != 0 && option.getMaxLength() != 0) {
                                            out.name("min_length").value(option.getMinLength());
                                            out.name("max_length").value(option.getMaxLength());
                                        }
                                    }

                                    out.endObject();
                                }
                            }

                            out.endArray();
                        }

                        out.endObject();
                    }
                }

                out.endArray();
            } else {
                if(!element.getOptions().isEmpty()) {
                    out.name("options");
                    out.beginArray();

                    for(SlashCommandOption src: element.getOptions()) {
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

                    out.endArray();
                }
            }
        }

        out.endObject();

        System.gc();
    }

    @Override
    public SlashCommand read(JsonReader in) throws IOException {
        return null;
    }
}
