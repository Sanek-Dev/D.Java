/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommandGroupBuilder {
    private String name;
    private String description;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private List<SubCommand> subCommands;

    public SubCommandGroupBuilder(String name, String description) {
        this.name = name;
        this.description = description;
        this.nameLocals = new HashMap<>();
        this.descLocals = new HashMap<>();
        this.subCommands = new ArrayList<>();
    }

    public SubCommandGroupBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SubCommandGroupBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SubCommandGroupBuilder addNameLocalization(DiscordLanguage language, String value) {
        this.nameLocals.put(language, value);
        return this;
    }

    public SubCommandGroupBuilder addDescriptionLocalization(DiscordLanguage language, String value) {
        this.descLocals.put(language, value);
        return this;
    }

    public SubCommandGroupBuilder addSubcommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    /**+
     * @return Built SubCommandGroup**/
    public SubCommandGroup build() {
        return new SubCommandGroup(name, description, nameLocals, descLocals, subCommands);
    }
}
