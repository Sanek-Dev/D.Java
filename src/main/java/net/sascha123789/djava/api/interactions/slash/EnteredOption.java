/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.fasterxml.jackson.databind.JsonNode;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;

public class EnteredOption {
    private SlashCommandOptionType type;
    private String name;
    private JsonNode value;

    public EnteredOption(SlashCommandOptionType type, String name, JsonNode value) {
        this.type = type;
        this.name = name;
        this.value = value;
    }

    public boolean getValueAsBoolean() {
        return value.asBoolean();
    }

    public String getValueAsString() {
        return value.asText();
    }

    public long getValueAsLong() {
        return value.asLong();
    }

    public int getValueAsInt() {
        return value.asInt();
    }

    public double getValueAsDouble() {
        return value.asDouble();
    }

    public SlashCommandOptionType getType() {
        return type;
    }

    public String getName() {
        return name;
    }
}
