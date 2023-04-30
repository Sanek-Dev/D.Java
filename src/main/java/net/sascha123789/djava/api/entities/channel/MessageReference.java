/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;

public class MessageReference {
    private String msgId;
    private String channelId;
    private String guildId;

    private MessageReference(String msgId, String channelId, String guildId) {
        this.msgId = msgId;
        this.channelId = channelId;
        this.guildId = guildId;
    }

    public static MessageReference fromJson(JsonObject json) {
        String msgId = "";
        if(json.get("message_id") != null) {
            if(!json.get("message_id").isJsonNull()) {
                msgId = json.get("message_id").getAsString();
            }
        }

        String channelId = "";
        if(json.get("channel_id") != null) {
            if(!json.get("channel_id").isJsonNull()) {
                channelId = json.get("channel_id").getAsString();
            }
        }

        String guildId = "";
        if(json.get("guild_id") != null) {
            if(!json.get("guild_id").isJsonNull()) {
                guildId = json.get("guild_id").getAsString();
            }
        }

        return new MessageReference(msgId, channelId, guildId);
    }

    public String getMessageId() {
        return msgId;
    }

    public String getChannelId() {
        return channelId;
    }

    public String getGuildId() {
        return guildId;
    }
}
