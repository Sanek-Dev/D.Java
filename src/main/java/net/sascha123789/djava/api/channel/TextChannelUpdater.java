/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class TextChannelUpdater {
    private String name;
    private String topic;
    private int position;
    private boolean nsfw;
    private int messageRate;
    private String parentId;
    private int archiveDuration;
    private DiscordClient client;
    private TextChannel channel;

    public TextChannelUpdater(DiscordClient client, TextChannel channel) {
        this.client = client;
        this.channel = channel;
        this.name = "";
        this.topic = "";
        this.position = -1;
        this.nsfw = false;
        this.messageRate = -1;
        this.parentId = "";
        this.archiveDuration = -1;
    }

    public void update() {
        JsonObject obj = new JsonObject();

        if(!name.isEmpty()) {
            obj.addProperty("name", name);
        }

        if(!topic.isEmpty()) {
            obj.addProperty("topic", topic);
        }

        if(position != -1) {
            obj.addProperty("position", position);
        }

        obj.addProperty("nsfw", nsfw);

        if(messageRate != -1) {
            obj.addProperty("rate_limit_per_user", messageRate);
        }

        if(archiveDuration != -1) {
            obj.addProperty("default_auto_archive_duration", archiveDuration);
        }

        if(!parentId.isEmpty()) {
            obj.addProperty("parent_id", parentId);
        }

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(obj.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/channels/" + channel.getId()))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body).thenAccept(ErrHandler::handle).join();
    }

    public TextChannelUpdater setName(String name) {
        this.name = name;
        return this;
    }

    public TextChannelUpdater setTopic(String topic) {
        this.topic = topic;
        return this;
    }

    public TextChannelUpdater setPosition(int position) {
        this.position = position;
        return this;
    }

    public TextChannelUpdater setNsfw(boolean nsfw) {
        this.nsfw = nsfw;
        return this;
    }

    /**
     * @param messageRate Amount of seconds a user has to wait before sending another message (0-21600); bots, as well as users with the permission manage_messages or manage_channel, are unaffected**/
    public TextChannelUpdater setMessageRate(int messageRate) {
        this.messageRate = messageRate;
        return this;
    }

    public TextChannelUpdater setParentCategoryId(String parentId) {
        this.parentId = parentId;
        return this;
    }

    /**
     * @param archiveDuration The default duration that the clients use (not the API) for newly created threads in the channel, in minutes, to automatically archive the thread after recent activity**/
    public TextChannelUpdater setArchiveDuration(int archiveDuration) {
        this.archiveDuration = archiveDuration;
        return this;
    }
}
