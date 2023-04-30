/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api;

import com.google.gson.JsonObject;
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

public class SelfUser extends User {
    private DiscordClient client;

    public SelfUser(DiscordClient client, String id, String username, String discriminator, String avatar, boolean bot, boolean system, boolean mfaEnabled, String banner, int accentColor, String locale, int flags, int publicFlags, int premiumType) {
        super(id, username, discriminator, avatar, bot, system, mfaEnabled, banner, accentColor, locale, flags, publicFlags, premiumType);
        this.client = client;
    }

    public static SelfUser fromJson(DiscordClient client, JsonObject json) {
        User user = Constants.GSON.fromJson(json, User.class);

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
            JsonObject obj = new JsonObject();

            if(!username.isEmpty()) {
                obj.addProperty("username", username);
            }

            if(avatarType == 1) {
                if(avatarFile != null) {
                    obj.addProperty("avatar", ImageUtils.toDataString(avatarFile, avatarImageType));
                }
            } else if(avatarType == 2) {
                if(!avatarUrl.isEmpty()) {
                    obj.addProperty("avatar", ImageUtils.toDataString(avatarUrl, avatarImageType));
                }
            }

            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/users/@me")
                    .patch(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                    .build();

            try(Response resp = client.getHttpClient().newCall(request).execute()) {
                String res = resp.body().string();
                ErrHandler.handle(res);
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
