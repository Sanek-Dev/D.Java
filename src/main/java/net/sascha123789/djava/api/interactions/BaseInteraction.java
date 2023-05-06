/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.Request;
import okhttp3.Response;

public abstract class BaseInteraction implements Identifiable {
    protected String id;
    protected String token;
    protected String appId;
    protected DiscordLanguage locale;
    protected DiscordClient client;
    protected BaseChannel channel;

    public BaseInteraction(DiscordClient client, String id, String token, String appId, DiscordLanguage locale, BaseChannel channel) {
        this.id = id;
        this.token = token;
        this.appId = appId;
        this.locale = locale;
        this.client = client;
        this.channel = channel;
    }

    public DiscordClient getClient() {
        return client;
    }

    /**
     * @return Channel that the interaction was sent from**/
    public BaseChannel getChannel() {
        return channel;
    }

    /**
     * @return Interaction response token**/
    public String getToken() {
        return token;
    }

    public String getApplicationId() {
        return appId;
    }

    /**
     * @return Selected language of the invoking user**/
    public DiscordLanguage getLocale() {
        return locale;
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
