/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.utils.Constants;

public class ForumTag implements Identifiable {
    private String id;
    private String name;
    private boolean moderated;
    private String emojiId;
    private String emojiName;

    private ForumTag(String id, String name, boolean moderated, String emojiId, String emojiName) {
        this.id = id;
        this.name = name;
        this.moderated = moderated;
        this.emojiId = emojiId;
        this.emojiName = emojiName;
    }

    public ForumTag setModerated(boolean moderated) {
        this.moderated = moderated;
        return this;
    }

    public static ForumTag create(String name, Emoji emoji) {
        return new ForumTag("", name, false, emoji.getId(), emoji.getName());
    }

    public JsonNode toJson() {
        ObjectNode obj = Constants.MAPPER.createObjectNode();

        if(!id.isEmpty()) {
            obj.put("id", id);
        }

        if(!name.isEmpty()) {
            obj.put("name", name);
        }

        obj.put("moderated", moderated);

        if(!emojiId.isEmpty()) {
            obj.put("emoji_id", emojiId);
        }

        if(!emojiName.isEmpty()) {
            obj.put("emoji_name", emojiName);
        }

        return obj;
    }

    public static ForumTag fromJson(JsonNode json) {
        String id = json.get("id").asText();
        String name = json.get("name").asText();
        boolean moderated = json.get("moderated").asBoolean();
        String emojiId = "";
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isNull()) {
                emojiId = json.get("emoji_id").asText();
            }
        }

        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isNull()) {
                emojiName = json.get("emoji_name").asText();
            }
        }

        return new ForumTag(id, name, moderated, emojiId, emojiName);
    }

    public String getName() {
        return name;
    }

    public boolean isModerated() {
        return moderated;
    }

    public String getEmojiId() {
        return emojiId;
    }

    public String getEmojiName() {
        return emojiName;
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
