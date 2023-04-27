/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommandBuilder {
    private String name;
    private String description;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private List<SlashCommandOption> options;

    public SubCommandBuilder(String name, String description) {
        this.name = name;
        this.description = description;
        this.nameLocals = new HashMap<>();
        this.descLocals = new HashMap<>();
        this.options = new ArrayList<>();
    }

    public SubCommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SubCommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SubCommandBuilder addNameLocalization(DiscordLanguage language, String value) {
        this.nameLocals.put(language, value);
        return this;
    }

    public SubCommandBuilder addDescriptionLocalization(DiscordLanguage language, String value) {
        this.descLocals.put(language, value);
        return this;
    }

    public SubCommandBuilder addOptions(SlashCommandOption... options) {
        this.options.addAll(List.of(options));
        return this;
    }

    public SubCommandBuilder addOptions(List<SlashCommandOption> options) {
        this.options.addAll(options);
        return this;
    }

    /**
     * @return Built SubCommand**/
    public SubCommand build() {
        return new SubCommand(name, description, nameLocals, descLocals, options);
    }
}
