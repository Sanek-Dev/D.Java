/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.channel.Message;

import java.lang.reflect.Type;

public class MessageDeserializer implements JsonDeserializer<Message> {

    @Override
    public Message deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = json.getAsJsonObject();
        String id = src.get("id").getAsString();
        String channelId = src.get("channel_id").getAsString();


        return null;
    }
}
