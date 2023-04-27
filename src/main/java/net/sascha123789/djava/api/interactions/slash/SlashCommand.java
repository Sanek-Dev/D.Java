/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;

import java.util.List;
import java.util.Map;

public class SlashCommand {
    private String id;
    private String appId;
    private String guildId;
    private String name;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private String description;
    private boolean dmPermission;
    private boolean nsfw;
    private List<DiscordPermission> requiredPermissions;
    private boolean availableForEveryone;
    private List<SubCommand> subCommands;
    private List<SubCommandGroup> groups;
    private List<SlashCommandOption> options;

    public SlashCommand(String id, String appId, String guildId, String name, Map<DiscordLanguage, String> nameLocals, Map<DiscordLanguage, String> descLocals, String description, boolean dmPermission, boolean nsfw, List<DiscordPermission> requiredPermissions, boolean availableForEveryone, List<SubCommand> subCommands, List<SubCommandGroup> groups, List<SlashCommandOption> options) {
        this.id = id;
        this.appId = appId;
        this.guildId = guildId;
        this.name = name;
        this.nameLocals = nameLocals;
        this.descLocals = descLocals;
        this.description = description;
        this.dmPermission = dmPermission;
        this.nsfw = nsfw;
        this.requiredPermissions = requiredPermissions;
        this.availableForEveryone = availableForEveryone;
        this.subCommands = subCommands;
        this.groups = groups;
        this.options = options;
    }

    /**
     * @return SlashCommand options**/
    public List<SlashCommandOption> getOptions() {
        return options;
    }

    /**
     * @return Subcommand Groups**/
    public List<SubCommandGroup> getSubcommandGroups() {
        return groups;
    }

    /**
     * @return SlashCommand subcommands**/
    public List<SubCommand> getSubCommands() {
        return subCommands;
    }

    /**
     * @return Indicates whether the command is age-restricted**/
    public boolean isNsfw() {
        return nsfw;
    }

    /**
     * @return Unique ID of command**/
    public String getId() {
        return id;
    }

    /**
     * @return ID of the parent application**/
    public String getApplicationId() {
        return appId;
    }

    /**
     * @return Guild ID of the command, if not global**/
    public String getGuildId() {
        return guildId;
    }

    /**
     * @return Name of command, 1-32 characters**/
    public String getName() {
        return name;
    }

    /**
     * @return Description of Slash Command**/
    public String getDescription() {
        return description;
    }

    /**
     * @return Localization dictionary for name field. Values follow the same restrictions as name**/
    public Map<DiscordLanguage, String> getNameLocalizations() {
        return nameLocals;
    }

    /**
     * @return Localization dictionary for description field. Values follow the same restrictions as description**/
    public Map<DiscordLanguage, String> getDescriptionLocalizations() {
        return descLocals;
    }

    /**
     * @return Indicates whether the command is available in DMs with the app, only for globally-scoped commands. By default, commands are visible.**/
    public boolean isDmPermission() {
        return dmPermission;
    }

    /**
     * @return Required permissions for use slash command**/
    public List<DiscordPermission> getRequiredPermissions() {
        return requiredPermissions;
    }

    public boolean isAvailableForEveryone() {
        return availableForEveryone;
    }
}
