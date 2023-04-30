/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageReactionRemoveAllEvent extends BaseEvent {
    private String channelId;
    private MessageableChannel channel;
    private String msgId;
    private Message message;
    private String guildId;

    public MessageReactionRemoveAllEvent(DiscordClient client, String channelId, String msgId, String guildId) {
        super(client);
        this.channelId = channelId;
        this.msgId = msgId;
        this.guildId = guildId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.message = channel.getMessageById(msgId).get();
    }

    public String getChannelId() {
        return channelId;
    }

    public MessageableChannel getChannel() {
        return channel;
    }

    public String getMsgId() {
        return msgId;
    }

    public Message getMessage() {
        return message;
    }

    public String getGuildId() {
        return guildId;
    }
}
