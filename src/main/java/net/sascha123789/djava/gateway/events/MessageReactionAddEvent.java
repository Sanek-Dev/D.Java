/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageReactionAddEvent extends BaseEvent {
    private String userId;
    private User user;
    private String channelId;
    private MessageableChannel channel;
    private String msgId;
    private Message message;
    private String guildId;
    private Emoji emoji;

    public MessageReactionAddEvent(DiscordClient client, String userId, String channelId, String msgId, String guildId, Emoji emoji) {
        super(client);
        this.userId = userId;
        this.user = client.getUserById(userId).get();
        this.channelId = channelId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.msgId = msgId;
        this.message = channel.getMessageById(msgId).get();
        this.guildId = guildId;
        this.emoji = emoji;
    }

    public String getUserId() {
        return userId;
    }

    public User getUser() {
        return user;
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

    public Emoji getEmoji() {
        return emoji;
    }
}
