/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.DiscordActioner;
import net.sascha123789.djava.gateway.DiscordClient;

public abstract class BaseEvent {
    protected DiscordClient client;
    protected DiscordActioner actioner;

    protected BaseEvent(DiscordClient client) {
        this.client = client;
        this.actioner = new DiscordActioner(client);
    }

    public DiscordClient getClient() {
        return client;
    }

    public DiscordActioner getActioner() {
        return actioner;
    }
}
