/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageReactionRemoveAllEmojiEvent extends BaseEvent {
    private String channelId;
    private MessageableChannel channel;
    private String guildId;
    private String msgId;
    private Message message;
    private Emoji emoji;

    public MessageReactionRemoveAllEmojiEvent(DiscordClient client, String channelId, String guildId, String msgId, Emoji emoji) {
        super(client);
        this.channelId = channelId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.guildId = guildId;
        this.msgId = msgId;
        this.message = channel.getMessageById(msgId).get();
        this.emoji = emoji;
    }

    public String getChannelId() {
        return channelId;
    }

    public MessageableChannel getChannel() {
        return channel;
    }

    public String getGuildId() {
        return guildId;
    }

    public String getMsgId() {
        return msgId;
    }

    public Message getMessage() {
        return message;
    }

    public Emoji getEmoji() {
        return emoji;
    }
}
