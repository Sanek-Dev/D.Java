/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.channel.TextChannel;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;

import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class TextChannelDeserializer implements JsonDeserializer<TextChannel> {
    private static boolean exists(JsonObject obj, String name) {
        if(obj.get(name) != null) {
            return !obj.get(name).isJsonNull();
        }
        return false;
    }

    @Override
    public TextChannel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        String id = src.get("id").getAsString();
        int position = 0;
        if(exists(src, "position")) {
            position = src.get("position").getAsInt();
        }

        String name = "";
        if(exists(src, "name")) {
            name = src.get("name").getAsString();
        }

        String topic = "";
        if(exists(src, "topic")) {
            topic = src.get("topic").getAsString();
        }

        boolean nsfw = false;
        if(exists(src, "nsfw")) {
            nsfw = src.get("nsfw").getAsBoolean();
        }

        String lastMsgId = "";
        if(exists(src, "last_message_id")) {
            lastMsgId = src.get("last_message_id").getAsString();
        }

        int messageRate = 0;
        if(exists(src, "rate_limit_per_user")) {
            messageRate = src.get("rate_limit_per_user").getAsInt();
        }

        String parentId = "";
        if(exists(src, "parent_id")) {
            parentId = src.get("parent_id").getAsString();
        }

        Timestamp lastPin = null;
        if(exists(src, "last_pin_timestamp")) {
            String str = src.get("last_pin_timestamp").getAsString();
            str = str.replace("+00:00", "");

            lastPin = Timestamp.valueOf(str);
        }

        int archiveDuration = 0;
        if(exists(src, "default_auto_archive_duration")) {
            archiveDuration = src.get("default_auto_archive_duration").getAsInt();
        }

        long flags = 0;
        if(exists(src, "flags")) {
            flags = src.get("flags").getAsLong();
        }

        List<ChannelFlag> f = new ArrayList<>();
        for(ChannelFlag el: ChannelFlag.values()) {
            if((el.getCode() & flags) == el.getCode()) {
                f.add(el);
            }
        }

        return new TextChannel(null, id, position, f, name, topic, nsfw, lastMsgId, messageRate, parentId, lastPin, archiveDuration, ChannelType.TEXT);
    }
}
