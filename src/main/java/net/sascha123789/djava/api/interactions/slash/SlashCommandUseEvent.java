/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.BaseInteraction;
import net.sascha123789.djava.gateway.DiscordClient;

public class SlashCommandUseEvent extends BaseInteraction {
    private String channelId;
    private String guildId;
    private DiscordLanguage guildLocale;
    private DiscordClient client;

    public DiscordLanguage getGuildLocale() {
        return guildLocale;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }

    public DiscordClient getClient() {
        return client;
    }

    public SlashCommandUseEvent(BaseChannel channel, DiscordClient client, String id, String token, String appId, DiscordLanguage locale, String channelId, String guildId, DiscordLanguage guildLocale) {
        super(client, id, token, appId, locale, channel);
        this.channelId = channelId;
        this.guildId = guildId;
        this.guildLocale = guildLocale;
        this.client = client;
    }
}
