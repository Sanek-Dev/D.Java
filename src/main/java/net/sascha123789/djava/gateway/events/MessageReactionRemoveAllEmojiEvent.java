/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageReactionRemoveAllEmojiEvent extends BaseEvent {
    private String channelId;
    private MessageableChannel channel;
    private Guild guild;
    private String msgId;
    private Message message;
    private Emoji emoji;

    public MessageReactionRemoveAllEmojiEvent(DiscordClient client, String channelId, Guild guild, String msgId, Emoji emoji) {
        super(client);
        this.channelId = channelId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.guild = guild;
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

    public Guild getGuild() {
        return guild;
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
