/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlashCommandOptionBuilder {
    private SlashCommandOptionType type;
    private String name;
    private String description;
    private final Map<DiscordLanguage, String> nameLocals;
    private final Map<DiscordLanguage, String> descLocals;
    private boolean required;
    private final List<OptionChoice> choices;
    private List<ChannelType> channelTypes;
    private int minValue;
    private int maxValue;
    private int minLength;
    private int maxLength;

    public SlashCommandOptionBuilder(SlashCommandOptionType type, String name, String description) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.nameLocals = new HashMap<>();
        this.descLocals = new HashMap<>();
        this.required = false;
        this.choices = new ArrayList<>();
        this.channelTypes = new ArrayList<>();
        this.minValue = 0;
        this.maxValue = 0;
        this.minLength = 0;
        this.maxLength = 0;
    }

    /**
     * Maximum length of a string value**/
    public SlashCommandOptionBuilder setMaxLength(int maxLength) {
        this.maxLength = maxLength;
        return this;
    }

    /**
     * Minimum length of a string value**/
    public SlashCommandOptionBuilder setMinLength(int minLength) {
        this.minLength = minLength;
        return this;
    }

    /**
     * Maximum integer or number value**/
    public SlashCommandOptionBuilder setMaxValue(int maxValue) {
        this.maxValue = maxValue;
        return this;
    }

    /**
     * Minimal integer or number value**/
    public SlashCommandOptionBuilder setMinValue(int minValue) {
        this.minValue = minValue;
        return this;
    }

    public SlashCommandOptionBuilder setChannelTypes(ChannelType... types) {
        this.channelTypes = List.of(types);
        return this;
    }

    public SlashCommandOptionBuilder setChannelTypes(List<ChannelType> types) {
        this.channelTypes = types;
        return this;
    }

    public SlashCommandOptionBuilder addChoice(String name, String value) {
        this.choices.add(new OptionChoice(name, value));
        return this;
    }

    public SlashCommandOptionBuilder addChoice(OptionChoice choice) {
        this.choices.add(choice);
        return this;
    }

    public SlashCommandOptionBuilder setRequired(boolean required) {
        this.required = required;
        return this;
    }

    public SlashCommandOptionBuilder addDescriptionLocalization(DiscordLanguage language, String value) {
        this.descLocals.put(language, value);
        return this;
    }

    public SlashCommandOptionBuilder addNameLocalization(DiscordLanguage language, String value) {
        this.nameLocals.put(language, value);
        return this;
    }

    public SlashCommandOptionBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SlashCommandOptionBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SlashCommandOptionBuilder setType(SlashCommandOptionType type) {
        this.type = type;
        return this;
    }

    /**
     * @return Built SlashCommandOption**/
    public SlashCommandOption build() {
        return new SlashCommandOption(type, name, description, nameLocals, descLocals, required, choices, channelTypes, minValue, maxValue, minLength, maxLength);
    }
}
