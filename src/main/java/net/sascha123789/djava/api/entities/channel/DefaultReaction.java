/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.utils.Constants;

public class DefaultReaction {
    private String emojiId;
    private String emojiName;

    private DefaultReaction(String emojiName, String emojiId) {
        this.emojiName = emojiName;
        this.emojiId = emojiId;
    }

    public static DefaultReaction create(Emoji emoji) {
        return new DefaultReaction(emoji.getName(), emoji.getId());
    }

    public JsonNode toJson() {
        ObjectNode obj = Constants.MAPPER.createObjectNode();

        if(!emojiId.isEmpty()) {
            obj.put("emoji_id", emojiId);
        }

        if(!emojiName.isEmpty()) {
            obj.put("emoji_name", emojiName);
        }

        return obj;
    }

    public static DefaultReaction fromJson(JsonNode json) {
        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isNull()) {
                emojiName = json.get("emoji_name").asText();
            }
        }

        String emojiId = "";
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isNull()) {
                emojiId = json.get("emoji_id").asText();
            }
        }

        return new DefaultReaction(emojiName, emojiId);
    }

    public String getEmojiId() {
        return emojiId;
    }

    public String getEmojiName() {
        return emojiName;
    }
}
