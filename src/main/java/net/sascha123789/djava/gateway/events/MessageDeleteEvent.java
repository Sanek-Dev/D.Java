/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageDeleteEvent extends BaseEvent implements Identifiable {
    private String id;
    private MessageableChannel channel;
    private String channelId;
    private String guildId;

    public MessageDeleteEvent(DiscordClient client, String id, String channelId, String guildId) {
        super(client);
        this.id = id;
        this.channelId = channelId;
        this.guildId = guildId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
    }

    public MessageableChannel getChannel() {
        return channel;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }
}
