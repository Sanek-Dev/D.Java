/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.gateway.DiscordClient;

import java.util.ArrayList;
import java.util.HashMap;
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
    private DiscordClient client;

    public static class Builder {
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
        private DiscordClient client;

        public Builder(DiscordClient client, String name, String description) {
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
            this.client = client;
        }

        public Builder addOptions(SlashCommandOption... options) {
            this.options.addAll(List.of(options));
            return this;
        }

        public Builder addOptions(List<SlashCommandOption> options) {
            this.options.addAll(options);
            return this;
        }

        public Builder setName(String name) {
            this.name = name;
            return this;
        }

        public Builder setDescription(String description) {
            this.description = description;
            return this;
        }

        public Builder addNameLocalization(DiscordLanguage language, String value) {
            this.nameLocals.put(language, value);
            return this;
        }

        public Builder addDescriptionLocalization(DiscordLanguage language, String value) {
            this.descLocals.put(language, value);
            return this;
        }

        public Builder setAvailableInDm(boolean dmPermission) {
            this.dmPermission = dmPermission;
            return this;
        }

        public Builder setNsfw(boolean nsfw) {
            this.nsfw = nsfw;
            return this;
        }

        public Builder addRequiredPermission(DiscordPermission permission) {
            this.permissions.add(permission);
            this.availableForEveryone = false;

            return this;
        }

        public Builder addSubcommand(SubCommand subCommand) {
            this.subCommands.add(subCommand);
            return this;
        }

        public Builder addSubcommandGroup(SubCommandGroup group) {
            this.groups.add(group);
            return this;
        }

        /**
         * @return Built SlashCommand**/
        public SlashCommand build() {
            return new SlashCommand(client, "RegId", "RegId", "RegId", name, nameLocals, descLocals, description, dmPermission, nsfw, permissions, availableForEveryone, subCommands, groups, options);
        }
    }

    public SlashCommand(DiscordClient client, String id, String appId, String guildId, String name, Map<DiscordLanguage, String> nameLocals, Map<DiscordLanguage, String> descLocals, String description, boolean dmPermission, boolean nsfw, List<DiscordPermission> requiredPermissions, boolean availableForEveryone, List<SubCommand> subCommands, List<SubCommandGroup> groups, List<SlashCommandOption> options) {
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
        this.client = client;
    }

    public DiscordClient getClient() {
        return client;
    }

    public String toMention() {
        return "</" + name + ":" + id + ">";
    }

    /**
     * @return SlashCommand options**/
    public ImmutableList<SlashCommandOption> getOptions() {
        return ImmutableList.copyOf(options);
    }

    /**
     * @return Subcommand Groups**/
    public ImmutableList<SubCommandGroup> getSubcommandGroups() {
        return ImmutableList.copyOf(groups);
    }

    /**
     * @return SlashCommand subcommands**/
    public ImmutableList<SubCommand> getSubCommands() {
        return ImmutableList.copyOf(subCommands);
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
    public ImmutableMap<DiscordLanguage, String> getNameLocalizations() {
        return ImmutableMap.copyOf(nameLocals);
    }

    /**
     * @return Localization dictionary for description field. Values follow the same restrictions as description**/
    public ImmutableMap<DiscordLanguage, String> getDescriptionLocalizations() {
        return ImmutableMap.copyOf(descLocals);
    }

    /**
     * @return Indicates whether the command is available in DMs with the app, only for globally-scoped commands. By default, commands are visible.**/
    public boolean isDmPermission() {
        return dmPermission;
    }

    /**
     * @return Required permissions for use slash command**/
    public ImmutableList<DiscordPermission> getRequiredPermissions() {
        return ImmutableList.copyOf(requiredPermissions);
    }

    public boolean isAvailableForEveryone() {
        return availableForEveryone;
    }
}
