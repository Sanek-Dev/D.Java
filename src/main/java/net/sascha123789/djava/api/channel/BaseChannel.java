/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.channel;

import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;

import java.util.List;

public abstract class BaseChannel implements Identifiable {
    protected String id;
    protected int position;
    protected List<ChannelFlag> flags;
    protected ChannelType type;
    protected DiscordClient client;

    public BaseChannel(DiscordClient client, String id, int position, List<ChannelFlag> flags, ChannelType type) {
        this.id = id;
        this.position = position;
        this.flags = flags;
        this.type = type;
        this.client = client;
    }

    /**
     * @apiNote Do not recommend to use this method!**/
    public BaseChannel setClient(DiscordClient client) {
        this.client = client;
        return this;
    }

    public ChannelType getType() {
        return type;
    }

    /**
     * @return Channel flags**/
    public List<ChannelFlag> getFlags() {
        return flags;
    }

    /**
     * @return Channel position**/
    public int getPosition() {
        return position;
    }

    public VoiceChannel getAsVoiceChannel() {
        return ((VoiceChannel) this);
    }

    public TextChannel getAsTextChannel() {
        return ((TextChannel) this);
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
