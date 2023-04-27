/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.channel;

import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.Timestamp;
import java.util.List;

public class TextChannel extends BaseChannel {
    private String name;
    private String topic;
    private boolean nsfw;
    private String lastMsgId;
    private int messageRate;
    private String parentId;
    private Timestamp lastPin;
    private int archiveDuration;

    public TextChannel(DiscordClient client, String id, int position, List<ChannelFlag> flags, String name, String topic, boolean nsfw, String lastMsgId, int messageRate, String parentId, Timestamp lastPin, int archiveDuration, ChannelType type)  {
        super(client, id, position, flags, type);
        this.name = name;
        this.topic = topic;
        this.nsfw = nsfw;
        this.lastMsgId = lastMsgId;
        this.messageRate = messageRate;
        this.parentId = parentId;
        this.lastPin = lastPin;
        this.archiveDuration = archiveDuration;
    }

    public void delete() {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/channels/" + id))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).thenAccept(ErrHandler::handle).join();
    }

    public TextChannelUpdater getUpdater() {
        return new TextChannelUpdater(client, this);
    }

    /**
     * @return Default duration, copied onto newly created threads, in minutes, threads will stop showing in the channel list after the specified period of inactivity, can be set to: 60, 1440, 4320, 10080**/
    public int getArchiveDuration() {
        return archiveDuration;
    }

    public Timestamp getLastPinTimestamp() {
        return lastPin;
    }

    /**
     * @return Channel parent category id**/
    public String getParentId() {
        return parentId;
    }

    /**
     * @return Amount of seconds a user has to wait before sending another message (0-21600); bots, as well as users with the permission manage_messages or manage_channel, are unaffected**/
    public int getMessageRate() {
        return messageRate;
    }

    public String getLastMessageId() {
        return lastMsgId;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    /**
     * @return Channel topic**/
    public String getTopic() {
        return topic;
    }

    /**
     * @return Channel name**/
    public String getName() {
        return name;
    }
}
