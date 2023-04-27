/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.gateway.DiscordClient;

public class HelloEvent extends BaseEvent {
    private int interval;

    public HelloEvent(DiscordClient client, int interval) {
        super(client);
        this.interval = interval;
    }

    public int getHeartbeatInterval() {
        return interval;
    }
}
