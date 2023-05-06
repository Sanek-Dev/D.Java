/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
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

            JsonNode arr = Constants.MAPPER.readTree(res);

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("webhook_channel_id", id);

        try {
            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + announcementChannelId + "/followers")
                    .post(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();

            try(Response resp = client.getHttpClient().newCall(request).execute()) {
                String res = resp.body().string();
                ErrHandler.handle(res);
            } catch(Exception e) {
                e.printStackTrace();
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Delete multiple messages in a single request**/
    public void bulkDeleteMessages(Collection<Message> collection) {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        ArrayNode arr = Constants.MAPPER.createArrayNode();

        for(Message msg: collection) {
            arr.add(msg.getId());
        }

        obj.set("messages", arr);

        try {
            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + id + "/messages/bulk-delete")
                    .post(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                    .build();

            try(Response resp = client.getHttpClient().newCall(request).execute()) {
                String res = resp.body().string();
                ErrHandler.handle(res);
            } catch(Exception e) {
                e.printStackTrace();
            }
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
        ObjectNode obj = Constants.MAPPER.createObjectNode();

        if(!data.getContent().isEmpty()) {
            obj.put("content", data.getContent());
        }
        obj.put("tts", data.isTts());

        if(!data.getEmbeds().isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();

            for(Embed emb: data.getEmbeds()) {
                arr.add(emb.toJson());
            }

            obj.set("embeds", arr);
        }

        if(data.getAllowedMentions() != null) {
            obj.set("allowed_mentions", data.getAllowedMentions().toJson());
        }

        if(data.getReference() != null) {
            ObjectNode o = Constants.MAPPER.createObjectNode();
            o.put("message_id", data.getReference().getMessageId());

            obj.set("message_reference", o);
        }

        if(!data.getStickers().isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();

            for(String el: data.getStickers()) {
                arr.add(el);
            }

            obj.set("sticker_ids", arr);
        }

        if(!data.getAttachments().isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();
            int i = 0;

            for(File el: data.getAttachments()) {
                ObjectNode o = Constants.MAPPER.createObjectNode();
                o.put("id", i);
                o.put("filename", el.getName());
                o.put("description", "");
                arr.add(o);

                i++;
            }

            obj.set("attachments", arr);
        }

        MultipartBody.Builder body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);

        try {
            body.addFormDataPart("payload_json", Constants.MAPPER.writeValueAsString(obj));
        } catch(Exception e) {
            e.printStackTrace();
        }

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
            return Optional.of(Message.fromJson(client, Constants.MAPPER.readTree(res)));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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

            JsonNode arr = Constants.MAPPER.readTree(str);

            Set<Message> set = new HashSet<>();

            for(JsonNode el: arr) {
                set.add(Message.fromJson(client, el));
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
            return Optional.of(Message.fromJson(client, Constants.MAPPER.readTree(res)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
