/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.managers.SlashCommandManager;
import net.sascha123789.djava.gateway.DiscordClient;

public abstract class BaseEvent {
    protected DiscordClient client;
    protected SlashCommandManager slashCommandManager;

    protected BaseEvent(DiscordClient client) {
        this.client = client;
        this.slashCommandManager = new SlashCommandManager(client);
    }

    public SlashCommandManager getSlashCommandManager() {
        return slashCommandManager;
    }

    public DiscordClient getClient() {
        return client;
    }
}
