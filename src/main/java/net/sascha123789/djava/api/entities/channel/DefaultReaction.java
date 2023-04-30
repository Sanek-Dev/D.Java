/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;

public class DefaultReaction {
    private String emojiId;
    private String emojiName;

    private DefaultReaction(String emojiName, String emojiId) {
        this.emojiName = emojiName;
        this.emojiId = emojiId;
    }

    public static DefaultReaction fromJson(JsonObject json) {
        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isJsonNull()) {
                emojiName = json.get("emoji_name").getAsString();
            }
        }

        String emojiId = "";
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isJsonNull()) {
                emojiId = json.get("emoji_id").getAsString();
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
