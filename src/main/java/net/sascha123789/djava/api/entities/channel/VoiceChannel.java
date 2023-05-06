/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.enums.ChannelFlag;
import net.sascha123789.djava.api.enums.ChannelType;
import net.sascha123789.djava.api.enums.VideoQualityMode;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.util.HashSet;
import java.util.Set;

public class VoiceChannel extends BaseChannel {
    private int bitrate;
    private int userLimit;
    private String rtcRegion;
    private VideoQualityMode videoQualityMode;

    private VoiceChannel(DiscordClient client, String id, ChannelType type, String guildId, int position, Set<PermissionOverwrite> permissionOverwrites, String name, boolean nsfw, String parentId, Set<ChannelFlag> flags, int bitrate, int userLimit, String rtcRegion, VideoQualityMode videoQualityMode) {
        super(client, id, type, guildId, position, permissionOverwrites, name, nsfw, parentId, flags);
        this.bitrate = bitrate;
        this.userLimit = userLimit;
        this.rtcRegion = rtcRegion;
        this.videoQualityMode = videoQualityMode;
    }

    public Updater getUpdater() {
        return new Updater(client, this);
    }

    public static class Updater {
        private DiscordClient client;
        private VoiceChannel channel;
        private String name;
        private int position;
        private boolean nsfw;
        private Set<PermissionOverwrite> overwrites;
        private String parentId;
        private Set<ChannelFlag> flags;
        private int bitrate;
        private int userLimit;
        private String rtcRegion;
        private VideoQualityMode videoQualityMode;

        public Updater(DiscordClient client, VoiceChannel channel) {
            this.client = client;
            this.channel = channel;
            this.name = "";
            this.position = -1;
            this.nsfw = false;
            this.overwrites = new HashSet<>();
            this.parentId = "";
            this.flags = new HashSet<>();
            this.bitrate = -1;
            this.userLimit = -1;
            this.rtcRegion = "";
            this.videoQualityMode = null;
        }

        public Updater setVideoQualityMode(VideoQualityMode videoQualityMode) {
            this.videoQualityMode = videoQualityMode;
            return this;
        }

        public Updater setRtcRegion(String rtcRegion) {
            this.rtcRegion = rtcRegion;
            return this;
        }

        public Updater setUserLimit(int limit) {
            this.userLimit = limit;
            return this;
        }

        public Updater setBitrate(int bitrate) {
            this.bitrate = bitrate;
            return this;
        }

        public Updater setFlags(Set<ChannelFlag> flags) {
            this.flags = flags;
            return this;
        }

        public Updater setParentId(String parentId) {
            this.parentId = parentId;
            return this;
        }

        public Updater removePermissionOverwrite(PermissionOverwrite overwrite) {
            this.overwrites.remove(overwrite);
            return this;
        }

        public Updater addPermissionOverwrite(PermissionOverwrite overwrite) {
            this.overwrites.add(overwrite);
            return this;
        }

        public Updater setNsfw(boolean nsfw) {
            this.nsfw = nsfw;
            return this;
        }

        public Updater setPosition(int position) {
            this.position = position;
            return this;
        }

        public Updater setName(String name) {
            this.name = name;
            return this;
        }

        public final void update() {
            ObjectNode obj = Constants.MAPPER.createObjectNode();

            if (!name.isEmpty()) {
                obj.put("name", name);
            }

            if (position != -1) {
                obj.put("position", Math.abs(position));
            }

            obj.put("nsfw", nsfw);

            if (!overwrites.isEmpty()) {
                ArrayNode arr = Constants.MAPPER.createArrayNode();

                for (PermissionOverwrite overwrite : overwrites) {
                    arr.add(overwrite.toJson());
                }

                obj.set("permission_overwrites", arr);
            }

            if (videoQualityMode != null) {
                obj.put("video_quality_mode", (videoQualityMode == VideoQualityMode.AUTO ? 1 : 2));
            }

            if (!rtcRegion.isEmpty()) {
                obj.put("rtc_region", rtcRegion);
            }

            if (userLimit != -1) {
                obj.put("user_limit", Math.abs(userLimit));
            }

            if (bitrate != -1) {
                obj.put("bitrate", Math.abs(bitrate));
            }

            if (!parentId.isEmpty()) {
                obj.put("parent_id", parentId);
            }

            if (!flags.isEmpty()) {
                long flags = 0;
                for (ChannelFlag el : this.flags) {
                    flags += el.getCode();
                }
                obj.put("flags", flags);
            }

             try {
                 Request request = new Request.Builder()
                         .url(Constants.BASE_URL + "/channels/" + channel.getId())
                         .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json"))).build();

                 try(Response resp = client.getHttpClient().newCall(request).execute()) {
                     String str = resp.body().string();

                     ErrHandler.handle(str);
                 } catch (Exception e) {
                     e.printStackTrace();
                 }
             } catch(Exception e) {
                 e.printStackTrace();
             }

            if(client.isOptimized()) {
                System.gc();
            }
        }
    }

        public static final VoiceChannel fromJson(DiscordClient client, JsonNode json) {
            /* Base */
            String id = json.get("id").asText();
            int t = json.get("type").asInt();
            ChannelType type = (t == 0 ? ChannelType.TEXT : (t == 2 ? ChannelType.VOICE : (t == 4 ? ChannelType.CATEGORY : (t == 5 ? ChannelType.ANNOUNCEMENT : (t == 10 ? ChannelType.ANNOUNCEMENT : (t == 11 ? ChannelType.PUBLIC_THREAD : (t == 12 ? ChannelType.PRIVATE_THREAD : (t == 13 ? ChannelType.STAGE : (t == 14 ? ChannelType.DIRECTORY : ChannelType.FORUM)))))))));
            String guildId = "";

            if(json.get("guild_id") != null) {
                if(!json.get("guild_id").isNull()) {
                    guildId = json.get("guild_id").asText();
                }
            }

            int position = 0;
            if(json.get("position") != null) {
                if(!json.get("position").isNull()) {
                    position = json.get("position").asInt();
                }
            }

            Set<PermissionOverwrite> overwrites = new HashSet<>();

            if(json.get("permission_overwrites") != null) {
                if(!json.get("permission_overwrites").isNull()) {
                    JsonNode arr = json.get("permission_overwrites");

                    for(JsonNode el: arr) {
                        overwrites.add(PermissionOverwrite.fromJson(el));
                    }
                }
            }

            String name = "";
            if(json.get("name") != null) {
                if(!json.get("name").isNull()) {
                    name = json.get("name").asText();
                }
            }

            boolean nsfw = false;
            if(json.get("nsfw") != null) {
                if(!json.get("nsfw").isNull()) {
                    nsfw = json.get("nsfw").asBoolean();
                }
            }

            String parentId = "";
            if(json.get("parent_id") != null) {
                if(!json.get("parent_id").isNull()) {
                    parentId = json.get("parent_id").asText();
                }
            }

            Set<ChannelFlag> flags = new HashSet<>();
            long flagsRaw = 0;
            if(json.get("flags") != null) {
                if(!json.get("flags").isNull()) {
                    flagsRaw = json.get("flags").asLong();
                }
            }

            for(ChannelFlag el: ChannelFlag.values()) {
                if((el.getCode() & flagsRaw) == el.getCode()) {
                    flags.add(el);
                }
            }
            /////////////////////////////////////////////////////

            int bitrate = 0;
            if (json.get("bitrate") != null) {
                if (!json.get("bitrate").isNull()) {
                    bitrate = json.get("bitrate").asInt();
                }
            }

            int userLimit = 0;
            if (json.get("user_limit") != null) {
                if (!json.get("user_limit").isNull()) {
                    userLimit = json.get("user_limit").asInt();
                }
            }

            String rtcRegion = "Automatic";
            if (json.get("rtc_region") != null) {
                if (!json.get("rtc_region").isNull()) {
                    rtcRegion = json.get("rtc_region").asText();
                }
            }

            int videoQ = 1;
            if (json.get("video_quality_mode") != null) {
                if (!json.get("video_quality_mode").isNull()) {
                    videoQ = json.get("video_quality_mode").asInt();
                }
            }
            VideoQualityMode videoQualityMode = (videoQ == 1 ? VideoQualityMode.AUTO : VideoQualityMode.FULL);

            if(client.isOptimized()) {
                System.gc();
            }

            return new VoiceChannel(client, id, type, guildId, position, overwrites, name, nsfw, parentId, flags, bitrate, userLimit, rtcRegion, videoQualityMode);
        }

        public int getBitrate() {
            return bitrate;
        }

        public int getUserLimit() {
            return userLimit;
        }

        public String getRtcRegion() {
            return rtcRegion;
        }

        public VideoQualityMode getVideoQualityMode() {
            return videoQualityMode;
        }
}