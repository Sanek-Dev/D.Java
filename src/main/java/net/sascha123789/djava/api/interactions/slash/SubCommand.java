/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.google.common.collect.ImmutableMap;
import net.sascha123789.djava.api.enums.DiscordLanguage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCommand {
    private String name;
    private String description;
    private Map<DiscordLanguage, String> nameLocals;
    private Map<DiscordLanguage, String> descLocals;
    private List<SlashCommandOption> options;

    public static class Builder {
        private String name;
        private String description;
        private Map<DiscordLanguage, String> nameLocals;
        private Map<DiscordLanguage, String> descLocals;
        private List<SlashCommandOption> options;

        public Builder(String name, String description) {
            this.name = name;
            this.description = description;
            this.nameLocals = new HashMap<>();
            this.descLocals = new HashMap<>();
            this.options = new ArrayList<>();
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

        public Builder addOptions(SlashCommandOption... options) {
            this.options.addAll(List.of(options));
            return this;
        }

        public Builder addOptions(List<SlashCommandOption> options) {
            this.options.addAll(options);
            return this;
        }

        /**
         * @return Built SubCommand**/
        public SubCommand build() {
            return new SubCommand(name, description, nameLocals, descLocals, options);
        }
    }

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

    public ImmutableMap<DiscordLanguage, String> getDescriptionLocalizations() {
        return ImmutableMap.copyOf(descLocals);
    }

    public ImmutableMap<DiscordLanguage, String> getNameLocalizations() {
        return ImmutableMap.copyOf(nameLocals);
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }
}
