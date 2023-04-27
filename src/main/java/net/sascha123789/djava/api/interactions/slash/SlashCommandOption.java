/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;

import java.util.List;
import java.util.Map;

public class SlashCommandOption {
    private SlashCommandOptionType type;
    private String name;
    private String description;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private boolean required;
    private List<OptionChoice> choices;
    private List<ChannelType> channelTypes;
    private int minValue;
    private int maxValue;
    private int minLength;
    private int maxLength;

    public SlashCommandOption(SlashCommandOptionType type, String name, String description, Map<DiscordLanguage, String> nameLocals, Map<DiscordLanguage, String> descLocals, boolean required, List<OptionChoice> choices, List<ChannelType> channelTypes, int minValue, int maxValue, int minLength, int maxLength) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.nameLocals = nameLocals;
        this.descLocals = descLocals;
        this.required = required;
        this.choices = choices;
        this.channelTypes = channelTypes;
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.minLength = minLength;
        this.maxLength = maxLength;
    }

    public int getMaxLength() {
        return maxLength;
    }

    public int getMinLength() {
        return minLength;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public List<ChannelType> getChannelTypes() {
        return channelTypes;
    }

    public List<OptionChoice> getChoices() {
        return choices;
    }

    public boolean isRequired() {
        return required;
    }

    public Map<DiscordLanguage, String> getDescriptionLocalizations() {
        return descLocals;
    }

    public Map<DiscordLanguage, String> getNameLocalizations() {
        return nameLocals;
    }

    public String getDescription() {
        return description;
    }

    public SlashCommandOptionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
