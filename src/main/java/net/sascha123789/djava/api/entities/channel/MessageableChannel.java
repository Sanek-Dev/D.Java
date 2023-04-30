/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public abstract class MessageableChannel extends BaseChannel {
    public MessageableChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
    }

    public void unpinMessage(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + this.id + "/pins/" + id)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void pinMessage(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + this.id + "/pins/" + id)
                .put(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Set<Message>> getPinnedMessages() {
        Set<Message> set = new HashSet<>();

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/pins")
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            JsonArray arr = Constants.GSON.fromJson(res, JsonArray.class);

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();
                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public void startTyping() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/typing")
                .post(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void followTo(String announcementChannelId) {
        JsonObject obj = new JsonObject();
        obj.addProperty("webhook_channel_id", id);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + announcementChannelId + "/followers")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete multiple messages in a single request**/
    public void bulkDeleteMessages(Collection<Message> collection) {
        JsonObject obj = new JsonObject();
        JsonArray arr = new JsonArray();

        for(Message msg: collection) {
            arr.add(msg.getId());
        }

        obj.add("messages", arr);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/messages/bulk-delete")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Message> sendMessage(String content) {
        MessageData data = new MessageData().setContent(content);
        return sendMessage(data);
    }

    public Optional<Message> sendMessage(Embed... embeds) {
        MessageData data = new MessageData().addEmbeds(embeds);

        return sendMessage(data);
    }

    public Optional<Message> sendMessage(MessageData data) {
        JsonObject obj = new JsonObject();

        if(!data.getContent().isEmpty()) {
            obj.addProperty("content", data.getContent());
        }
        obj.addProperty("tts", data.isTts());

        if(!data.getEmbeds().isEmpty()) {
            JsonArray arr = new JsonArray();

            for(Embed emb: data.getEmbeds()) {
                arr.add(emb.toJson());
            }

            obj.add("embeds", arr);
        }

        if(data.getAllowedMentions() != null) {
            obj.add("allowed_mentions", data.getAllowedMentions().toJson());
        }

        if(data.getReference() != null) {
            JsonObject o = new JsonObject();
            o.addProperty("message_id", data.getReference().getMessageId());

            obj.add("message_reference", o);
        }

        if(!data.getStickers().isEmpty()) {
            JsonArray arr = new JsonArray();

            for(String el: data.getStickers()) {
                arr.add(el);
            }

            obj.add("sticker_ids", arr);
        }

        if(!data.getAttachments().isEmpty()) {
            JsonArray arr = new JsonArray();
            int i = 0;

            for(File el: data.getAttachments()) {
                JsonObject o = new JsonObject();
                o.addProperty("id", i);
                o.addProperty("filename", el.getName());
                o.addProperty("description", "");
                arr.add(o);

                i++;
            }

            obj.add("attachments", arr);
        }

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        body.addFormDataPart("payload_json", obj.toString());

        if(!data.getAttachments().isEmpty()) {
            int i = 0;
            for(File file: data.getAttachments()) {
                body.addFormDataPart("files[" + i + "]", file.getName(), RequestBody.create(MediaType.parse("image/png"), file));

                i++;
            }
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + id + "/messages")
                .post(body.build())
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            return Optional.of(Message.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
        }

        return Optional.empty();
    }

    public Optional<Set<Message>> getMessagesAfter(String id, int limit) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("after", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessagesAfter(String id) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", "100")
                .addQueryParameter("after", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessagesBefore(String id, int limit) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("before", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessagesBefore(String id) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(100))
                .addQueryParameter("before", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessagesAround(String id, int limit) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(limit))
                .addQueryParameter("around", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessagesAround(String id) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(100))
                .addQueryParameter("around", id);

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessages(int limit) {
        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(limit));

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Set<Message>> getMessages() {

        HttpUrl.Builder b = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id + "/messages").newBuilder()
                .addQueryParameter("limit", String.valueOf(100));

        Request request = new Request.Builder()
                .url(b.build().toString())
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);

            JsonArray arr = Constants.GSON.fromJson(str, JsonArray.class);

            Set<Message> set = new HashSet<>();

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();

                set.add(Message.fromJson(client, o));
            }

            return Optional.of(set);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Message> getMessageById(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + this.id + "/messages/" + id)
                .get().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            return Optional.of(Message.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
