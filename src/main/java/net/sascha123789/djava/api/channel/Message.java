/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.channel;

import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.enums.MessageType;

import java.sql.Timestamp;
import java.util.List;

public class Message implements Identifiable {
    private String id;
    private String channelId;
    private User author;
    private String content;
    private Timestamp sentTimestamp;
    private Timestamp editedTimestamp;
    private boolean tts;
    private boolean mentionEveryone;
    private List<User> mentionUsers;
    private List<BaseChannel> mentionChannels;
    private BaseChannel channel;
    private boolean pinned;
    private String webhookId;
    private MessageType type;

    public Message(String id, String channelId, User author, String content, Timestamp sentTimestamp, Timestamp editedTimestamp, boolean tts, boolean mentionEveryone, List<User> mentionUsers, List<BaseChannel> mentionChannels, BaseChannel channel, boolean pinned, String webhookId, MessageType type) {
        this.id = id;
        this.channelId = channelId;
        this.author = author;
        this.content = content;
        this.sentTimestamp = sentTimestamp;
        this.editedTimestamp = editedTimestamp;
        this.tts = tts;
        this.mentionEveryone = mentionEveryone;
        this.mentionUsers = mentionUsers;
        this.mentionChannels = mentionChannels;
        this.channel = channel;
        this.pinned = pinned;
        this.webhookId = webhookId;
        this.type = type;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public Timestamp getEditedTimestamp() {
        return editedTimestamp;
    }

    public boolean isTts() {
        return tts;
    }

    public boolean isMentionEveryone() {
        return mentionEveryone;
    }

    public List<User> getMentionUsers() {
        return mentionUsers;
    }

    public List<BaseChannel> getMentionChannels() {
        return mentionChannels;
    }

    public BaseChannel getChannel() {
        return channel;
    }

    public boolean isPinned() {
        return pinned;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public MessageType getType() {
        return type;
    }

    public String getChannelId() {
        return channelId;
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
