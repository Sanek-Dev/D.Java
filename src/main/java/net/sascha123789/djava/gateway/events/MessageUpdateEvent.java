/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageUpdateEvent extends MessageCreateEvent {
    public MessageUpdateEvent(DiscordClient client, Message msg, Guild guild) {
        super(client, msg, guild);
    }
}
