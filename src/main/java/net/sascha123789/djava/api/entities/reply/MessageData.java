/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.reply;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.channel.Embed;
import net.sascha123789.djava.api.entities.channel.MessageReference;
import net.sascha123789.djava.api.entities.channel.Sticker;
import net.sascha123789.djava.utils.Constants;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import java.io.File;
import java.util.*;

public class MessageData {
    private String content;
    private Set<Embed> embeds;
    private boolean tts;
    private AllowedMentions allowedMentions;
    private MessageReference reference;
    private List<String> stickers;
    private Set<File> attachments;
    private boolean ephemeral;
    private int type;

    public MessageData() {
        this.content = "";
        this.embeds = new HashSet<>();
        this.tts = false;
        this.allowedMentions = null;
        this.reference = null;
        this.stickers = new ArrayList<>();
        this.attachments = new HashSet<>();
        this.ephemeral = false;
        this.type = -1;
    }

    public void setType(int type) {
        this.type = type;
    }

    public MessageData setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }

    public MessageData setContent(String content) {
        this.content = content;
        return this;
    }

    public MessageData addEmbeds(Embed... embeds) {
        this.embeds.addAll(List.of(embeds));
        return this;
    }

    public MessageData setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    public MessageData setAllowedMentions(AllowedMentions allowedMentions) {
        this.allowedMentions = allowedMentions;
        return this;
    }

    public MessageData setReplyMessage(String id) {
        ObjectNode o = Constants.MAPPER.createObjectNode();
        o.put("message_id", id);
        this.reference = MessageReference.fromJson(o);
        return this;
    }

    public MessageData addSticker(Sticker sticker) {
        this.stickers.add(sticker.getId());
        return this;
    }

    public MessageData addAttachment(File file) {
        this.attachments.add(file);
        return this;
    }

    public String getContent() {
        return content;
    }

    public Set<Embed> getEmbeds() {
        return embeds;
    }

    public boolean isTts() {
        return tts;
    }

    public AllowedMentions getAllowedMentions() {
        return allowedMentions;
    }

    public MessageReference getReference() {
        return reference;
    }

    public List<String> getStickers() {
        return stickers;
    }

    public Set<File> getAttachments() {
        return attachments;
    }
}
