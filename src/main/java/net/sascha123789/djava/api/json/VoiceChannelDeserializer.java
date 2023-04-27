/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.json;

import com.google.gson.*;
import net.sascha123789.djava.api.channel.VoiceChannel;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.VideoQualityMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class VoiceChannelDeserializer implements JsonDeserializer<VoiceChannel> {
    private static boolean exists(JsonObject obj, String name) {
        if(obj.get(name) != null) {
            return !obj.get(name).isJsonNull();
        }

        return false;
    }

    @Override
    public VoiceChannel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject src = new JsonObject();
        String id = src.get("id").getAsString();
        int position = 0;
        if(exists(src, "position")) {
            position = src.get("position").getAsInt();
        }
        String name = "";
        if(exists(src, "name")) {
            name = src.get("name").getAsString();
        }

        boolean nsfw = false;
        if(exists(src, "nsfw")) {
            nsfw = src.get("nsfw").getAsBoolean();
        }

        String topic = "";
        if(exists(src, "topic")) {
            topic = src.get("topic").getAsString();
        }

        int bitrate = 0;
        if(exists(src, "bitrate")) {
            bitrate = src.get("bitrate").getAsInt();
        }

        int userLimit = 0;
        if(exists(src, "user_limit")) {
            userLimit = src.get("user_limit").getAsInt();
        }

        String rtcRegion = "Automatic";
        if(exists(src, "rtc_region")) {
            rtcRegion = src.get("rtc_region").getAsString();
        }

        int videoQualityModeRaw = 0;
        if(exists(src, "video_quality_mode")) {
            videoQualityModeRaw = src.get("video_quality_mode").getAsInt();
        }

        VideoQualityMode qualityMode = null;
        if(videoQualityModeRaw == 1) {
            qualityMode = VideoQualityMode.AUTO;
        } else {
            qualityMode = VideoQualityMode.FULL;
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

        return new VoiceChannel(null, id, position, f, name, topic, nsfw, bitrate, userLimit, rtcRegion, qualityMode, ChannelType.VOICE);
    }
}
