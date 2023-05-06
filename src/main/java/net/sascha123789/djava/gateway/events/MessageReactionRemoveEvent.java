/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.gateway.DiscordClient;

public class MessageReactionRemoveEvent extends BaseEvent {
    private String userId;
    private User user;
    private String channelId;
    private MessageableChannel channel;
    private String messageId;
    private Message message;
    private Guild guild;
    private Emoji emoji;

    public MessageReactionRemoveEvent(DiscordClient client, String userId, String channelId, String messageId, Guild guild, Emoji emoji) {
        super(client);
        this.userId = userId;
        this.user = client.getUserById(userId).get();
        this.channelId = channelId;
        this.channel = client.getChannelById(channelId).get().asMessageable();
        this.messageId = messageId;
        this.message = channel.getMessageById(messageId).get();
        this.guild = guild;
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

    public String getMessageId() {
        return messageId;
    }

    public Message getMessage() {
        return message;
    }

    public Guild getGuild() {
        return guild;
    }

    public Emoji getEmoji() {
        return emoji;
    }
}
