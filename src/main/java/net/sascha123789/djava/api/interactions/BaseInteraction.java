/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.interactions;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Embed;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.reply.InteractionMessageData;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
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

    public void editReply(String content) {
        editReply(new InteractionMessageData().setContent(content));
    }

    public void editReply(Embed... embeds) {
        editReply(new InteractionMessageData().addEmbeds(embeds));
    }

    public Message replyFollowup(String content, boolean ephemeral) {
        InteractionMessageData data = new InteractionMessageData();
        data.setContent(content);
        data.setEphemeral(ephemeral);

        return replyFollowup(data);
    }
    
    public Message replyFollowup(boolean ephemeral, Embed... embeds) {
        InteractionMessageData data = new InteractionMessageData();
        data.setEphemeral(ephemeral);
        data.addEmbeds(embeds);

        return replyFollowup(data);
    }

    public Message reply(boolean ephemeral, Embed... embeds) {
        InteractionMessageData data = new InteractionMessageData();
        data.setEphemeral(ephemeral);
        data.addEmbeds(embeds);

        return reply(data);
    }

    public Message reply(String content, boolean ephemeral) {
        InteractionMessageData data = new InteractionMessageData();
        data.setContent(content);
        data.setEphemeral(ephemeral);

        return reply(data);
    }

    public Message replyFollowup(Embed... embeds) {
        InteractionMessageData data = new InteractionMessageData();
        data.addEmbeds(embeds);
        return replyFollowup(data);
    }

    public Message replyFollowup(String content) {
        InteractionMessageData data = new InteractionMessageData();
        data.setContent(content);

        return replyFollowup(data);
    }

    public Message reply(Embed... embeds) {
        InteractionMessageData data = new InteractionMessageData();
        data.addEmbeds(embeds);

        return reply(data);
    }

    public Message reply(String content) {
        InteractionMessageData data = new InteractionMessageData();
        data.setContent(content);

        return reply(data);
    }

    public Message replyFollowup(InteractionMessageData data) {
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/webhooks/" + client.getApplicationId() + "/" + token)
                    .post(RequestBody.create(Constants.MAPPER.writeValueAsString(data.toJson()), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Message.fromJson(client, Constants.MAPPER.readTree(res));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void editReply(InteractionMessageData data) {
        Request request = null;
        try {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/webhooks/" + client.getApplicationId() + "/" + token + "/messages/@original")
                    .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(data.toJson()), MediaType.parse("application/json")))
                    .build();
        } catch(Exception e) {
            e.printStackTrace();
            return;
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void replyDefer() {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("type", 5);

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
    }

    public Message reply(InteractionMessageData data) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("type", 4);
        obj.set("data", data.toJson());

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
