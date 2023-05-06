/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions.slash;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableMap;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.channel.MessageableChannel;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.BaseInteraction;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.Map;

public class SlashCommandUseEvent extends BaseInteraction {
    private final MessageableChannel channel;
    private Guild guild;
    private final DiscordLanguage guildLocale;
    private final DiscordClient client;
    private final Member self;
    private final String name;
    private final String groupName;
    private final String subcommandName;
    private Map<String, EnteredOption> options;

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

    /**
     * Respond to an autocomplete interaction with suggested choices**/
    public Message replyAutocomplete() {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("type", 8);

        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/interactions/" + id + "/" + token + "/callback")
                    .post(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        Request req = new Request.Builder()
                .url(Constants.BASE_URL + "/webhooks/" + client.getApplicationId() + "/" + token + "/messages/@original")
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(req).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Message.fromJson(client, Constants.MAPPER.readTree(res));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String getSubcommandName() {
        return subcommandName;
    }

    public String getSubcommandGroupName() {
        return groupName;
    }

    public String getName() {
        return name;
    }

    public ImmutableMap<String, EnteredOption> getOptions() {
        return ImmutableMap.copyOf(options);
    }

    public SlashCommandUseEvent(Map<String, EnteredOption> options, String subcommandName, String groupName, String name, BaseChannel channel, DiscordClient client, String id, String token, String appId, DiscordLanguage locale, String channelId, String guildId, DiscordLanguage guildLocale, Member self) {
        super(client, id, token, appId, locale, channel);
        this.channel = channel.asMessageable();

        try {
            this.guild = client.getCacheManager().getGuildCache().get(guildId);
        } catch(Exception e) {
            e.printStackTrace();
        }
        this.self = self;

        this.guildLocale = guildLocale;
        this.client = client;
        this.name = name;
        this.subcommandName = subcommandName;
        this.groupName = groupName;
        this.options = options;
    }
}
