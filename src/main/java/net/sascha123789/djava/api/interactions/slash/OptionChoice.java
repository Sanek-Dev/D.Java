/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;

import java.util.HashMap;
import java.util.Map;

public class OptionChoice {
    private String name;
    private Map<DiscordLanguage, String> nameLocals;
    private String value;

    public OptionChoice(String name, String value) {
        this.name = name;
        this.value = value;
        this.nameLocals = new HashMap<>();
    }

    public Map<DiscordLanguage, String> getNameLocalizations() {
        return nameLocals;
    }

    public String getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public OptionChoice addNameLocalization(DiscordLanguage language, String value) {
        this.nameLocals.put(language, value);
        return this;
    }
}
