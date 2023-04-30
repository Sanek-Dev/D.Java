/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageCreateEvent extends BaseEvent{
    private Message msg;
    private String guildId;
    private MessageableChannel channel;

    public MessageCreateEvent(DiscordClient client, Message msg, String guildId) {
        super(client);
        this.channel = client.getChannelById(msg.getChannelId()).get().asMessageable();
        this.msg = msg;
        this.guildId = guildId;
    }

    public MessageableChannel getChannel() {
        return channel;
    }

    public Message getMessage() {
        return msg;
    }

    public String getGuildId() {
        return guildId;
    }
}
