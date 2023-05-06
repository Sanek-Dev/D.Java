/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static class Builder {
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

        public Builder(SlashCommandOptionType type, String name, String description) {
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
        public Builder setMaxLength(int maxLength) {
            this.maxLength = maxLength;
            return this;
        }

        /**
         * Minimum length of a string value**/
        public Builder setMinLength(int minLength) {
            this.minLength = minLength;
            return this;
        }

        /**
         * Maximum integer or number value**/
        public Builder setMaxValue(int maxValue) {
            this.maxValue = maxValue;
            return this;
        }

        /**
         * Minimal integer or number value**/
        public Builder setMinValue(int minValue) {
            this.minValue = minValue;
            return this;
        }

        public Builder setChannelTypes(ChannelType... types) {
            this.channelTypes = List.of(types);
            return this;
        }

        public Builder setChannelTypes(List<ChannelType> types) {
            this.channelTypes = types;
            return this;
        }

        public Builder addChoice(String name, String value) {
            this.choices.add(new OptionChoice(name, value));
            return this;
        }

        public Builder addChoice(OptionChoice choice) {
            this.choices.add(choice);
            return this;
        }

        public Builder setRequired(boolean required) {
            this.required = required;
            return this;
        }

        public Builder addDescriptionLocalization(DiscordLanguage language, String value) {
            this.descLocals.put(language, value);
            return this;
        }

        public Builder addNameLocalization(DiscordLanguage language, String value) {
            this.nameLocals.put(language, value);
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setType(SlashCommandOptionType type) {
            this.type = type;
            return this;
        }

        /**
         * @return Built SlashCommandOption**/
        public SlashCommandOption build() {
            return new SlashCommandOption(type, name, description, nameLocals, descLocals, required, choices, channelTypes, minValue, maxValue, minLength, maxLength);
        }
    }

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

    public ImmutableList<ChannelType> getChannelTypes() {
        return ImmutableList.copyOf(channelTypes);
    }

    public ImmutableList<OptionChoice> getChoices() {
        return ImmutableList.copyOf(choices);
    }

    public boolean isRequired() {
        return required;
    }

    public ImmutableMap<DiscordLanguage, String> getDescriptionLocalizations() {
        return ImmutableMap.copyOf(descLocals);
    }

    public ImmutableMap<DiscordLanguage, String> getNameLocalizations() {
        return ImmutableMap.copyOf(nameLocals);
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
