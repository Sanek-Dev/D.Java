/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.channel;

import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.VideoQualityMode;
import net.sascha123789.djava.gateway.DiscordClient;

import java.util.List;

public class VoiceChannel extends BaseChannel {
    private String name;
    private boolean nsfw;
    private String topic;
    private int bitrate;
    private int userLimit;
    private String rtcRegion;
    private VideoQualityMode videoQualityMode;

    public VoiceChannel(DiscordClient client, String id, int position, List<ChannelFlag> flags, String name, String topic, boolean nsfw, int bitrate, int userLimit, String rtcRegion, VideoQualityMode videoQualityMode, ChannelType type) {
        super(client, id, position, flags, type);
        this.name = name;
        this.nsfw = nsfw;
        this.topic = topic;
        this.bitrate = bitrate;
        this.userLimit = userLimit;
        this.rtcRegion = rtcRegion;
        this.videoQualityMode = videoQualityMode;
    }

    public VideoQualityMode getVideoQualityMode() {
        return videoQualityMode;
    }

    public String getRtcRegion() {
        return rtcRegion;
    }

    public int getUserLimit() {
        return userLimit;
    }

    public int getBitrate() {
        return bitrate;
    }

    public boolean isNsfw() {
        return nsfw;
    }

    public String getTopic() {
        return topic;
    }

    public String getName() {
        return name;
    }
}
