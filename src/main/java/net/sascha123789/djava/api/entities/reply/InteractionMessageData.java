/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.reply;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sascha123789.djava.api.entities.channel.Embed;
import net.sascha123789.djava.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class InteractionMessageData {
    private String content;
    private AllowedMentions allowedMentions;
    private boolean tts;
    private final List<Embed> embeds;
    private boolean ephemeral;

    public InteractionMessageData() {
        this.content = "";
        this.allowedMentions = null;
        this.tts = false;
        this.embeds = new ArrayList<>();
        this.ephemeral = false;
    }

    public JsonNode toJson() {
        ObjectNode obj = Constants.MAPPER.createObjectNode();
        obj.put("tts", tts);

        if(!content.isEmpty()) {
            obj.put("content", content);
        }

        if(allowedMentions != null) {
            obj.set("allowed_mentions", allowedMentions.toJson());
        }

        if(!embeds.isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();

            for(Embed emb: embeds) {
                arr.add(emb.toJson());
            }

            obj.set("embeds", arr);
        }

        if(ephemeral) {
            obj.put("flags", 1 << 6);
        }

        return obj;
    }

    public String getContent() {
        return content;
    }

    public InteractionMessageData setContent(String content) {
        this.content = content;
        return this;
    }

    public AllowedMentions getAllowedMentions() {
        return allowedMentions;
    }

    public InteractionMessageData setAllowedMentions(AllowedMentions allowedMentions) {
        this.allowedMentions = allowedMentions;
        return this;
    }

    public boolean isTts() {
        return tts;
    }

    public InteractionMessageData setTts(boolean tts) {
        this.tts = tts;
        return this;
    }

    public List<Embed> getEmbeds() {
        return embeds;
    }

    public InteractionMessageData addEmbeds(Embed... embeds) {
        this.embeds.addAll(List.of(embeds));
        return this;
    }

    public boolean isEphemeral() {
        return ephemeral;
    }

    public InteractionMessageData setEphemeral(boolean ephemeral) {
        this.ephemeral = ephemeral;
        return this;
    }
}
