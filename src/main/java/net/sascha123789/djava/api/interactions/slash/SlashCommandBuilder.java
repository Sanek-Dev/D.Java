/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SlashCommandBuilder {
    private String name;
    private final Map<DiscordLanguage, String> nameLocals;
    private final Map<DiscordLanguage, String> descLocals;
    private String description;
    private boolean dmPermission;
    private boolean nsfw;
    private final List<DiscordPermission> permissions;
    private boolean availableForEveryone;
    private List<SubCommand> subCommands;
    private List<SubCommandGroup> groups;
    private List<SlashCommandOption> options;

    public SlashCommandBuilder(String name, String description) {
        this.name = name;
        this.description = description;
        this.nameLocals = new HashMap<>();
        this.descLocals = new HashMap<>();
        this.dmPermission = false;
        this.nsfw = false;
        this.permissions = new ArrayList<>();
        this.availableForEveryone = true;
        this.subCommands = new ArrayList<>();
        this.groups = new ArrayList<>();
        this.options = new ArrayList<>();
    }

    public SlashCommandBuilder addOptions(SlashCommandOption... options) {
        this.options.addAll(List.of(options));
        return this;
    }

    public SlashCommandBuilder addOptions(List<SlashCommandOption> options) {
        this.options.addAll(options);
        return this;
    }

    public SlashCommandBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SlashCommandBuilder setDescription(String description) {
        this.description = description;
        return this;
    }

    public SlashCommandBuilder addNameLocalization(DiscordLanguage language, String value) {
        this.nameLocals.put(language, value);
        return this;
    }

    public SlashCommandBuilder addDescriptionLocalization(DiscordLanguage language, String value) {
        this.descLocals.put(language, value);
        return this;
    }

    public SlashCommandBuilder setAvailableInDm(boolean dmPermission) {
        this.dmPermission = dmPermission;
        return this;
    }

    public SlashCommandBuilder setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    public SlashCommandBuilder addRequiredPermission(DiscordPermission permission) {
        this.permissions.add(permission);
        this.availableForEveryone = false;

        return this;
    }

    public SlashCommandBuilder addSubcommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
        return this;
    }

    public SlashCommandBuilder addSubcommandGroup(SubCommandGroup group) {
        this.groups.add(group);
        return this;
    }

    /**
     * @return Built SlashCommand**/
    public SlashCommand build() {
        return new SlashCommand("RegId", "RegId", "RegId", name, nameLocals, descLocals, description, dmPermission, nsfw, permissions, availableForEveryone, subCommands, groups, options);
    }
}
