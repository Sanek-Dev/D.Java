/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageCreateEvent extends BaseEvent {
    private final Message msg;
    private final Guild guild;
    private final MessageableChannel channel;

    public MessageCreateEvent(DiscordClient client, Message msg, Guild guild) {
        super(client);
        this.msg = msg;
        this.guild = guild;
        this.channel = client.getCacheManager().getChannelCache().getUnchecked(msg.getChannelId()).asMessageable();
    }

    public final MessageableChannel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return msg;
    }

    public final Guild getGuild() {
        return guild;
    }
}
