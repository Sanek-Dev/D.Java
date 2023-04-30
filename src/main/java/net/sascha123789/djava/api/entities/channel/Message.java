/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.DeferInstance;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;

import java.io.File;
import java.sql.Timestamp;
import java.util.*;

public class Message implements Identifiable, DeferInstance<Message> {
    private String id;
    private String channelId;
    private User author;
    private String content;
    private Timestamp sentTimestamp;
    private Timestamp editTimestamp;
    private boolean tts;
    private boolean mentionEveryone;
    private DiscordClient client;
    private Set<User> mentionUsers;
    private Set<String> mentionRoles;
    private Set<ChannelMention> mentionChannels;
    private Set<Attachment> attachments;
    private Set<Embed> embeds;
    private Set<Reaction> reactions;
    private boolean pinned;
    private String webhookId;
    private MessageType type;
    private MessageReference reference;
    private List<MessageFlag> flags;
    private Message referenceMsg;
    private Set<StickerItem> stickers;

    private Message(String id, String channelId, User author, String content, Timestamp sentTimestamp, Timestamp editTimestamp, boolean tts, boolean mentionEveryone, Set<Attachment> attachments, Set<Embed> embeds, Set<Reaction> reactions, boolean pinned, String webhookId, MessageType type, MessageReference reference, List<MessageFlag> flags, Message referenceMsg, Set<StickerItem> stickers, Set<User> mentionUsers, Set<String> mentionRoles, DiscordClient client, Set<ChannelMention> mentionChannels) {
        this.id = id;
        this.channelId = channelId;
        this.author = author;
        this.content = content;
        this.sentTimestamp = sentTimestamp;
        this.editTimestamp = editTimestamp;
        this.tts = tts;
        this.mentionEveryone = mentionEveryone;
        this.attachments = attachments;
        this.embeds = embeds;
        this.reactions = reactions;
        this.pinned = pinned;
        this.webhookId = webhookId;
        this.type = type;
        this.reference = reference;
        this.flags = flags;
        this.referenceMsg = referenceMsg;
        this.stickers = stickers;
        this.client = client;
        this.mentionRoles = mentionRoles;
        this.mentionUsers = mentionUsers;
        this.mentionChannels = mentionChannels;
    }

    public Optional<Message> reply(String content) {
        MessageData data = new MessageData();
        data.setContent(content);
        data.setReplyMessage(id);
        BaseChannel channel = client.getChannelById(channelId).get();

        try {
            MessageableChannel el = channel.asMessageable();
            return el.sendMessage(data);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Message> reply(Embed... embeds) {
        MessageData data = new MessageData();
        data.setReplyMessage(id);
        data.addEmbeds(embeds);
        BaseChannel channel = client.getChannelById(channelId).get();

        try {
            MessageableChannel el = channel.asMessageable();
            return el.sendMessage(data);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Message> reply(MessageData data) {
        data.setReplyMessage(id);
        BaseChannel channel = client.getChannelById(channelId).get();

        try {
            MessageableChannel el = channel.asMessageable();
            return el.sendMessage(data);
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Message crosspost() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/crosspost")
                .post(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Optional<ThreadChannel> startThread(String name) {
        JsonObject obj = new JsonObject();
        obj.addProperty("name", name);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/threads")
                .post(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(ThreadChannel.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Message unpin() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/pins/" + id)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public Message pin() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/pins/" + id)
                .put(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
        return this;
    }

    public void delete() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(String reason) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id)
                .delete()
                .addHeader("X-Audit-Log-Reason", reason)
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Optional<Message> edit(String content) {
        return edit(new MessageData().setContent(content));
    }

    public Optional<Message> edit(Embed... embeds) {
        return edit(new MessageData().addEmbeds(embeds));
    }

    public Optional<Message> edit(MessageData data) {
        JsonObject obj = new JsonObject();

        if(!data.getContent().isEmpty()) {
            obj.addProperty("content", data.getContent());
        }

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
                .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id)
                .patch(body.build())
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            return Optional.of(Message.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Message deleteAllReactions() {
        Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions")
                    .delete()
                    .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Message deleteAllReactionsByEmoji(Emoji emoji) {
        Request request = null;
        if(emoji.isUnicode()) {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.getName())
                    .delete()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.toUrlString())
                    .delete()
                    .build();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Message copyReactedUsersTo(Emoji reactEmoji, Collection<User> collection) {
        Request request = null;
        if(reactEmoji.isUnicode()) {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + reactEmoji.getName())
                    .get()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + reactEmoji.toUrlString())
                    .get()
                    .build();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            JsonArray arr = Constants.GSON.fromJson(res, JsonArray.class);

            for(JsonElement el: arr) {
                JsonObject o = el.getAsJsonObject();
                User user = Constants.GSON.fromJson(o, User.class);

                collection.add(user);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Message removeReaction(Emoji emoji, String userId) {
        Request request = null;
        if(emoji.isUnicode()) {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.getName() + "/" + userId)
                    .delete()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.toUrlString() + "/" + userId)
                    .delete()
                    .build();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Message removeReaction(Emoji emoji) {
        Request request = null;
        if(emoji.isUnicode()) {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.getName() + "/@me")
                    .delete()
                    .build();
        } else {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.toUrlString() + "/@me")
                    .delete()
                    .build();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    /**
     * @return Current instance**/
    public Message addReaction(Emoji emoji) {

        Request request = null;
        if(emoji.isUnicode()) {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.getName() + "/@me")
                    .put(RequestBody.create(emoji.toJson().toString(), MediaType.parse("application/json")))
                    .build();
        } else {
            request = new Request.Builder()
                    .url(Constants.BASE_URL + "/channels/" + channelId + "/messages/" + id + "/reactions/" + emoji.toUrlString() + "/@me")
                    .put(RequestBody.create(emoji.toJson().toString(), MediaType.parse("application/json")))
                    .build();
        }

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }

        return this;
    }

    public Set<ChannelMention> getMentionChannels() {
        return mentionChannels;
    }

    public static Message fromJson(DiscordClient client, JsonObject json) {
        String id = json.get("id").getAsString();
        String channelId = json.get("channel_id").getAsString();
        User author = null;

        try {
            author = Constants.GSON.fromJson(json.get("author").getAsJsonObject(), User.class);
        } catch(Exception ignored) {

        }

        String content = "";

        if(json.get("content") != null) {
            if(!json.get("content").isJsonNull()) {
                content = json.get("content").getAsString();
            }
        }

        Timestamp sentTimestamp = null;
        if(json.get("timestamp") != null) {
            if(!json.get("timestamp").isJsonNull()) {
                String sentS = json.get("timestamp").getAsString();
                sentS = sentS.replace("+00:00", "");
                sentS = sentS.replace("T", " ");
                sentTimestamp = Timestamp.valueOf(sentS);
            }
        }

        Timestamp editTimestamp = null;
        if(json.get("edited_timestamp") != null) {
            if(!json.get("edited_timestamp").isJsonNull()) {
                String s = json.get("edited_timestamp").getAsString();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                editTimestamp = Timestamp.valueOf(s);
            }
        }

        boolean tts = json.get("tts").getAsBoolean();
        boolean mentionEveryone = json.get("mention_everyone").getAsBoolean();
        Set<User> mentionUsers = new HashSet<>();
        JsonArray usersArr = json.get("mentions").getAsJsonArray();

        for(JsonElement el: usersArr) {
            JsonObject o = el.getAsJsonObject();

            mentionUsers.add(Constants.GSON.fromJson(o, User.class));
        }

        Set<String> mentionRoles = new HashSet<>();
        JsonArray rolesArr = json.get("mention_roles").getAsJsonArray();

        for(JsonElement el: rolesArr) {
            mentionRoles.add(el.getAsString());
        }

        Set<Attachment> attachments = new HashSet<>();
        JsonArray attachmentsArr = json.get("attachments").getAsJsonArray();

        for(JsonElement el: attachmentsArr) {
            JsonObject o = el.getAsJsonObject();

            attachments.add(Attachment.fromJson(o));
        }

        Set<Embed> embeds = new HashSet<>();
        JsonArray embedArr = json.get("embeds").getAsJsonArray();

        for(JsonElement el: embedArr) {
            JsonObject o = el.getAsJsonObject();

            embeds.add(Embed.fromJson(o));
        }

        Set<Reaction> reactions = new HashSet<>();

        if(json.get("reactions") != null) {
            if(!json.get("reactions").isJsonNull()) {
                JsonArray arr = json.get("reactions").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();

                    reactions.add(Reaction.fromJson(client, o));
                }
            }
        }

        boolean pinned = json.get("pinned").getAsBoolean();

        String webhookId = "";
        if(json.get("webhook_id") != null) {
            if(!json.get("webhook_id").isJsonNull()) {
                webhookId = json.get("webhook_id").getAsString();
            }
        }

        MessageType type = null;
        for(MessageType el: MessageType.values()) {
            if(el.getCode() == json.get("type").getAsInt()) {
                type = el;
                break;
            }
        }

        MessageReference reference = null;
        if(json.get("message_reference") != null) {
            if(!json.get("message_reference").isJsonNull()) {
                reference = MessageReference.fromJson(json.get("message_reference").getAsJsonObject());
            }
        }

        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isJsonNull()) {
                flagsRaw = json.get("flags").getAsLong();
            }
        }

        List<MessageFlag> flags = new ArrayList<>();
        for(MessageFlag el: MessageFlag.values()) {
            if((el.getCode() & flagsRaw) == el.getCode()) {
                flags.add(el);
            }
        }

        Message referenceMsg = null;
        if(json.get("referenced_message") != null) {
            if(!json.get("referenced_message").isJsonNull()) {
                referenceMsg = Message.fromJson(client, json.get("referenced_message").getAsJsonObject());
            }
        }

        Set<StickerItem> stickers = new HashSet<>();

        if(json.get("sticker_items") != null) {
            if(!json.get("sticker_items").isJsonNull()) {
                JsonArray arr = json.get("sticker_items").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();
                    stickers.add(StickerItem.fromJson(o));
                }
            }
        }

        Set<ChannelMention> mentionChannels = new HashSet<>();

        if(json.get("mention_channels") != null) {
            if(!json.get("mention_channels").isJsonNull()) {
                JsonArray arr = json.get("mention_channels").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();
                    mentionChannels.add(ChannelMention.fromJson(o));
                }
            }
        }

        return new Message(id, channelId, author, content, sentTimestamp, editTimestamp, tts, mentionEveryone, attachments, embeds, reactions, pinned, webhookId, type, reference, flags, referenceMsg, stickers, mentionUsers, mentionRoles, client, mentionChannels);
    }

    public String getChannelId() {
        return channelId;
    }

    public User getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public Timestamp getSentTimestamp() {
        return sentTimestamp;
    }

    public Timestamp getEditTimestamp() {
        return editTimestamp;
    }

    public boolean isTts() {
        return tts;
    }

    public boolean isMentionEveryone() {
        return mentionEveryone;
    }

    public DiscordClient getClient() {
        return client;
    }

    public Set<User> getMentionUsers() {
        return mentionUsers;
    }

    public Set<String> getMentionRoles() {
        return mentionRoles;
    }

    public Set<Attachment> getAttachments() {
        return attachments;
    }

    public Set<Embed> getEmbeds() {
        return embeds;
    }

    public Set<Reaction> getReactions() {
        return reactions;
    }

    public boolean isPinned() {
        return pinned;
    }

    public String getWebhookId() {
        return webhookId;
    }

    public MessageType getType() {
        return type;
    }

    public MessageReference getReference() {
        return reference;
    }

    public List<MessageFlag> getFlags() {
        return flags;
    }

    public Message getReferenceMsg() {
        return referenceMsg;
    }

    public Set<StickerItem> getStickers() {
        return stickers;
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
