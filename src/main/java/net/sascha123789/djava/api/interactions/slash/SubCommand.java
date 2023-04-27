/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;

import java.util.List;
import java.util.Map;

public class SubCommand {
    private String name;
    private String description;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private List<SlashCommandOption> options;

    public SubCommand(String name, String description, Map<DiscordLanguage, String> nameLocals, Map<DiscordLanguage, String> descLocals, List<SlashCommandOption> options) {
        this.name = name;
        this.description = description;
        this.nameLocals = nameLocals;
        this.descLocals = descLocals;
        this.options = options;
    }

    public List<SlashCommandOption> getOptions() {
        return options;
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

    public String getName() {
        return name;
    }
}
