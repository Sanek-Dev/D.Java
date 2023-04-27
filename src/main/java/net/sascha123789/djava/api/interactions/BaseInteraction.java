/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions;

import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.channel.BaseChannel;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.gateway.DiscordClient;

public abstract class BaseInteraction implements Identifiable {
    protected String id;
    protected String token;
    protected String appId;
    protected DiscordLanguage locale;
    protected BaseChannel channel;
    protected DiscordClient client;

    public BaseInteraction(DiscordClient client, String id, String token, String appId, DiscordLanguage locale, BaseChannel channel) {
        this.id = id;
        this.token = token;
        this.appId = appId;
        this.locale = locale;
        this.channel = channel;
        this.client = client;
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
