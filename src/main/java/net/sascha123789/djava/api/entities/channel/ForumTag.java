/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;

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

    public static ForumTag fromJson(JsonObject json) {
        String id = json.get("id").getAsString();
        String name = json.get("name").getAsString();
        boolean moderated = json.get("moderated").getAsBoolean();
        String emojiId = "";
        if(json.get("emoji_id") != null) {
            if(!json.get("emoji_id").isJsonNull()) {
                emojiId = json.get("emoji_id").getAsString();
            }
        }

        String emojiName = "";
        if(json.get("emoji_name") != null) {
            if(!json.get("emoji_name").isJsonNull()) {
                emojiName = json.get("emoji_name").getAsString();
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
