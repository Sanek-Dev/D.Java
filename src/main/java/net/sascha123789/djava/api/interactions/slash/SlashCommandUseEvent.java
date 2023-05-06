/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.BaseInteraction;
import net.sascha123789.djava.gateway.DiscordClient;

public class SlashCommandUseEvent extends BaseInteraction {
    private final MessageableChannel channel;
    private Guild guild;
    private final DiscordLanguage guildLocale;
    private final DiscordClient client;
    private final Member self;

    public DiscordLanguage getGuildLocale() {
        return guildLocale;
    }

    public Guild getGuild() {
        return guild;
    }

    @Override
    public MessageableChannel getChannel() {
        return channel;
    }

    public DiscordClient getClient() {
        return client;
    }

    public Member getSelfMember() {
        return self;
    }

    public SlashCommandUseEvent(BaseChannel channel, DiscordClient client, String id, String token, String appId, DiscordLanguage locale, String channelId, String guildId, DiscordLanguage guildLocale, Member self) {
        super(client, id, token, appId, locale, channel);
        this.channel = client.getCacheManager().getChannelCache().getUnchecked(channelId).asMessageable();

        try {
            this.guild = client.getCacheManager().getGuildCache().get(guildId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.self = self;

        this.guildLocale = guildLocale;
        this.client = client;
    }
}
