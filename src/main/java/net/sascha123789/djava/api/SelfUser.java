/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import net.sascha123789.djava.utils.ImageUtils;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

public class SelfUser extends User {
    private DiscordClient client;

    public SelfUser(DiscordClient client, String id, String username, String discriminator, String avatar, boolean bot, boolean system, boolean mfaEnabled, String banner, int accentColor, String locale, int flags, int publicFlags, int premiumType) {
        super(id, username, discriminator, avatar, bot, system, mfaEnabled, banner, accentColor, locale, flags, publicFlags, premiumType);
        this.client = client;
    }

    public static SelfUser fromJson(DiscordClient client, JsonNode json) {
        User user = User.fromJson(json);

        return new SelfUser(client, user.getId(), user.getUsername(), user.getDiscriminator(), user.getAvatarHash(), user.isBot(), user.isSystem(), user.isMfaEnabled(), user.getBannerHash(), user.getAccentColorRaw(), user.getLocaleRaw(), user.getFlagsRaw(), user.getPublicFlagsRaw(), user.getNitroTypeRaw());
    }

    public void leaveFromGuild(String guildId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/users/@me/guilds/" + guildId)
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public Updater getUpdater() {
        return new Updater(client);
    }

    public static class Updater {
        private String username;
        private File avatarFile;
        private String avatarUrl;
        private byte avatarType;
        private ImageType avatarImageType;
        private DiscordClient client;

        public Updater(DiscordClient client) {
            this.username = "";
            this.avatarFile = null;
            this.avatarUrl = "";
            this.avatarType = 0;
            this.client = client;
        }

        public Updater setUsername(String username) {
            this.username = username;
            return this;
        }

        public Updater setAvatar(String url, ImageType type) {
            this.avatarUrl = url;
            this.avatarImageType = type;
            this.avatarType = 2;
            return this;
        }

        public Updater setAvatar(File file, ImageType type) {
            this.avatarFile = file;
            this.avatarType = 1;
            this.avatarImageType = type;
            return this;
        }

        public void update() {
            ObjectNode obj = Constants.MAPPER.createObjectNode();

            if(!username.isEmpty()) {
                obj.put("username", username);
            }

            if(avatarType == 1) {
                if(avatarFile != null) {
                    obj.put("avatar", ImageUtils.toDataString(avatarFile, avatarImageType));
                }
            } else if(avatarType == 2) {
                if(!avatarUrl.isEmpty()) {
                    obj.put("avatar", ImageUtils.toDataString(avatarUrl, avatarImageType));
                }
            }

            try {
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL + "/users/@me")
                        .patch(RequestBody.create(Constants.MAPPER.writeValueAsString(obj), MediaType.parse("application/json")))
                        .build();

                try(Response resp = client.getHttpClient().newCall(request).execute()) {
                    String res = resp.body().string();
                    ErrHandler.handle(res);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void leaveThread(String threadId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + threadId + "/thread-members/@me")
                .delete()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void joinThread(String threadId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/channels/" + threadId + "/thread-members/@me")
                .put(RequestBody.create("{}", MediaType.parse("application/json")))
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
