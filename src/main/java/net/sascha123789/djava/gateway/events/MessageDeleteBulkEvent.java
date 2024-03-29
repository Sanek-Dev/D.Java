/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.gateway.DiscordClient;

import java.util.Set;

public class MessageDeleteBulkEvent extends BaseEvent {
    private Set<String> ids;
    private String channelId;
    private MessageableChannel channel;
    private Guild guild;

    public MessageDeleteBulkEvent(DiscordClient client, Set<String> ids, String channelId, Guild guild) {
        super(client);
        this.ids = ids;
        this.channelId = channelId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.guild = guild;
    }

    public Set<String> getIds() {
        return ids;
    }

    public String getChannelId() {
        return channelId;
    }

    public MessageableChannel getChannel() {
        return channel;
    }

    public Guild getGuild() {
        return guild;
    }
}
