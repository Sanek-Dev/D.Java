/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.reply.MessageData;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.DeferInstance;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.StringReader;
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

    @Override
    public boolean equals(Object o) {
        if(o == null) return false;

        if(o.getClass() != this.getClass()) return false;

        return ((Message) o).getId().equals(this.id);
    }

    private void initClient(DiscordClient client) {
        this.client = client;
    }

    @Override
    public String toString() {
        return content;
    }

    public Message(String id, String channelId, User author, String content, Timestamp sentTimestamp, Timestamp editTimestamp, boolean tts, boolean mentionEveryone, Set<Attachment> attachments, Set<Embed> embeds, Set<Reaction> reactions, boolean pinned, String webhookId, MessageType type, MessageReference reference, List<MessageFlag> flags, Message referenceMsg, Set<StickerItem> stickers, Set<User> mentionUsers, Set<String> mentionRoles, DiscordClient client, Set<ChannelMention> mentionChannels) {
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

            return Optional.of(ThreadChannel.fromJson(client, Constants.MAPPER.readTree(res)));
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
        ObjectNode obj = Constants.MAPPER.createObjectNode();

        if(!data.getContent().isEmpty()) {
            obj.put("content", data.getContent());
        }

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
            return Optional.of(Message.fromJson(client, Constants.MAPPER.readTree(res)));
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

    public ImmutableSet<ChannelMention> getMentionChannels() {
        return ImmutableSet.copyOf(mentionChannels);
    }

    public static Message fromJson(DiscordClient client, JsonNode json) {
        String id = json.get("id").asText();
        String channelId = json.get("channel_id").asText();
        User author = null;

        if(json.get("author") != null) {
            author = User.fromJson(json.get("author"));
        }

        String content = "";

        if(json.get("content") != null) {
            if(!json.get("content").isNull()) {
                content = json.get("content").asText();
            }
        }

        Timestamp sentTimestamp = null;
        if(json.get("timestamp") != null) {
            if(!json.get("timestamp").isNull()) {
                String sentS = json.get("timestamp").asText();
                sentS = StringUtils.replace(sentS,  "+00:00", "");
                sentS = StringUtils.replace(sentS, "T", " ");
                sentTimestamp = Timestamp.valueOf(sentS);
            }
        }

        Timestamp editTimestamp = null;
        if(json.get("edited_timestamp") != null) {
            if(!json.get("edited_timestamp").isNull()) {
                String s = json.get("edited_timestamp").asText();
                s = StringUtils.replace(s, "+00:00", "");
                s = StringUtils.replace(s, "T", " ");
                editTimestamp = Timestamp.valueOf(s);
            }
        }

        boolean tts = false;

        if(json.get("tts") != null) {
            if(!json.get("tts").isNull()) {
                tts = json.get("tts").asBoolean();
            }
        }

        boolean mentionEveryone = false;

        if(json.get("mention_everyone") != null) {
            if(!json.get("mention_everyone").isNull()) {
                mentionEveryone = json.get("mention_everyone").asBoolean();
            }
        }

        Set<User> mentionUsers = new HashSet<>();
        JsonNode usersArr = json.get("mentions");

        if(usersArr != null) {
            for(JsonNode el: usersArr) {
                mentionUsers.add(User.fromJson(el));
            }
        }

        Set<String> mentionRoles = new HashSet<>();
        JsonNode rolesArr = json.get("mention_roles");

        if(rolesArr != null) {
            for(JsonNode el: rolesArr) {
                mentionRoles.add(el.asText());
            }
        }

        Set<Attachment> attachments = new HashSet<>();
        JsonNode attachmentsArr = json.get("attachments");

        if(attachmentsArr != null) {
            for(JsonNode el: attachmentsArr) {
                attachments.add(Attachment.fromJson(el));
            }
        }

        Set<Embed> embeds = new HashSet<>();
        JsonNode embedArr = json.get("embeds");

        if(embedArr != null) {
            for(JsonNode el: embedArr) {
                embeds.add(Embed.fromJson(el));
            }
        }

        Set<Reaction> reactions = new HashSet<>();

        if(json.get("reactions") != null) {
            if(!json.get("reactions").isNull()) {
                JsonNode arr = json.get("reactions");

                for(JsonNode el: arr) {
                    reactions.add(Reaction.fromJson(client, el));
                }
            }
        }

        boolean pinned = false;
        if(json.get("pinned") != null) {
            pinned = json.get("pinned").asBoolean();
        }

        String webhookId = "";
        if(json.get("webhook_id") != null) {
            if(!json.get("webhook_id").isNull()) {
                webhookId = json.get("webhook_id").asText();
            }
        }

        MessageType type = null;
        for(MessageType el: MessageType.values()) {
            if(el.getCode() == json.get("type").asInt()) {
                type = el;
                break;
            }
        }

        MessageReference reference = null;
        if(json.get("message_reference") != null) {
            if(!json.get("message_reference").isNull()) {
                reference = MessageReference.fromJson(json.get("message_reference"));
            }
        }

        long flagsRaw = 0;
        if(json.get("flags") != null) {
            if(!json.get("flags").isNull()) {
                flagsRaw = json.get("flags").asLong();
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
            if(!json.get("referenced_message").isNull()) {
                referenceMsg = Message.fromJson(client, json.get("referenced_message"));
            }
        }

        Set<StickerItem> stickers = new HashSet<>();

        if(json.get("sticker_items") != null) {
            if(!json.get("sticker_items").isNull()) {
                JsonNode arr = json.get("sticker_items");

                for(JsonNode el: arr) {
                    stickers.add(StickerItem.fromJson(el));
                }
            }
        }

        Set<ChannelMention> mentionChannels = new HashSet<>();

        if(json.get("mention_channels") != null) {
            if(!json.get("mention_channels").isNull()) {
                JsonNode arr = json.get("mention_channels");

                for(JsonNode el: arr) {
                    mentionChannels.add(ChannelMention.fromJson(el));
                }
            }
        }

        if(client.isOptimized()) {
            System.gc();
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

    public ImmutableSet<User> getMentionUsers() {
        return ImmutableSet.copyOf(mentionUsers);
    }

    public ImmutableSet<String> getMentionRoles() {
        return ImmutableSet.copyOf(mentionRoles);
    }

    public ImmutableSet<Attachment> getAttachments() {
        return ImmutableSet.copyOf(attachments);
    }

    public ImmutableSet<Embed> getEmbeds() {
        return ImmutableSet.copyOf(embeds);
    }

    public ImmutableSet<Reaction> getReactions() {
        return ImmutableSet.copyOf(reactions);
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

    public ImmutableList<MessageFlag> getFlags() {
        return ImmutableList.copyOf(flags);
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
