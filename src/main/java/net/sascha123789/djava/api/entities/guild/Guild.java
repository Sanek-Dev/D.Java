/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.*;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.entities.channel.*;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.DiscordLanguage;
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
import java.util.stream.Collectors;

public class Guild implements Identifiable {
    private final DiscordClient client;
    private final String id;
    private final String name;
    private final String iconHash;
    private final String splash;
    private final String discoverySplash;
    private final Member owner;
    private final VoiceChannel afkChannel;
    private final int afkTimeout;
    private final boolean widgetEnabled;
    private final BaseChannel widgetChannel;
    private final VerificationLevel verificationLevel;
    private final DefaultNotificationLevel defaultNotificationLevel;
    private final ExplicitContentFilterLevel explicitContentFilterLevel;
    private final Set<Role> roles;
    private final Set<Emoji> emojis;
    private final Set<GuildFeature> features;
    private final MfaLevel mfaLevel;
    private final BaseChannel systemChannel;
    private final Set<SystemChannelFlag> systemChannelFlags;
    private final MessageableChannel rulesChannel;
    private final int maxMembers;
    private final String vanityCode;
    private final String description;
    private final String banner;
    private final GuildBoostTier boostTier;
    private final int boostCount;
    private final DiscordLanguage preferredLang;
    private final MessageableChannel updatesChannel;
    private final int maxVideoUsers;
    private final int maxStageVideoUsers;
    private final int memberCount;
    private final int presenceCount;
    private final GuildWelcomeScreen welcomeScreen;
    private final NsfwLevel nsfwLevel;
    private final Set<Sticker> stickers;
    private final boolean boostsBarEnabled;

    private Guild(DiscordClient client, String id, String name, String icon, String splash, String discoverySplash, String ownerId, VoiceChannel afkChannel, int afkTimeout, boolean widgetEnabled, BaseChannel widgetChannel, VerificationLevel verificationLevel, DefaultNotificationLevel defaultNotificationLevel, ExplicitContentFilterLevel explicitContentFilterLevel, Set<Role> roles, Set<Emoji> emojis, Set<GuildFeature> features, MfaLevel mfaLevel, BaseChannel systemChannel, Set<SystemChannelFlag> systemChannelFlags, MessageableChannel rulesChannel, int maxMembers, String vanityCode, String description, String banner, GuildBoostTier boostTier, int boostCount, DiscordLanguage preferredLang, MessageableChannel updatesChannel, int maxVideoUsers, int maxStageVideoUsers, int memberCount, int presenceCount, GuildWelcomeScreen welcomeScreen, NsfwLevel nsfwLevel, Set<Sticker> stickers, boolean boostsBarEnabled) {
        this.id = id;
        this.client = client;
        this.name = name;
        this.iconHash = icon;
        this.splash = splash;
        this.discoverySplash = discoverySplash;
        this.owner = this.getMemberById(ownerId);
        this.afkTimeout = afkTimeout;
        this.afkChannel = afkChannel;
        this.widgetEnabled = widgetEnabled;
        this.widgetChannel = widgetChannel;
        this.verificationLevel = verificationLevel;
        this.defaultNotificationLevel = defaultNotificationLevel;
        this.explicitContentFilterLevel = explicitContentFilterLevel;
        this.roles = roles;
        this.emojis = emojis;
        this.features = features;
        this.mfaLevel = mfaLevel;
        this.systemChannel = systemChannel;
        this.systemChannelFlags = systemChannelFlags;
        this.rulesChannel = rulesChannel;
        this.maxMembers = maxMembers;
        this.vanityCode = vanityCode;
        this.description = description;
        this.banner = banner;
        this.boostTier = boostTier;
        this.boostCount = boostCount;
        this.preferredLang = preferredLang;
        this.updatesChannel = updatesChannel;
        this.maxVideoUsers = maxVideoUsers;
        this.maxStageVideoUsers = maxStageVideoUsers;
        this.memberCount = memberCount;
        this.presenceCount = presenceCount;
        this.welcomeScreen = welcomeScreen;
        this.nsfwLevel = nsfwLevel;
        this.stickers = stickers;
        this.boostsBarEnabled = boostsBarEnabled;
    }

    public Updater getUpdater() {
        return new Updater(this);
    }

    public static class Updater {
        private Guild guild;
        private String name;
        private int verifyLevel;
        private int defaultNotifyLevel;
        private int explicitLevel;
        private VoiceChannel afkChannel;
        private int afkTimeout;
        private int iconType;
        private File iconFile;
        private String iconUrl;
        private ImageType icon;
        private Member owner;
        private int splashType;
        private File splashFile;
        private String splashUrl;
        private ImageType splash;
        private int discoverySplashType;
        private File discoverySplashFile;
        private String discoverySplashUrl;
        private ImageType discoverySplash;
        private int bannerType;
        private File bannerFile;
        private String bannerUrl;
        private ImageType banner;
        private BaseChannel systemChannel;
        private Set<SystemChannelFlag> systemChannelFlags;
        private MessageableChannel rulesChannel;
        private MessageableChannel updatesChannel;
        private String locale;
        private Set<GuildFeature> features;
        private String description;
        private boolean boostBarEnabled;

        public Updater(Guild guild) {
            this.guild = guild;
            this.name = "";
            this.verifyLevel = -1;
            this.defaultNotifyLevel = -1;
            this.explicitLevel = -1;
            this.afkChannel = null;
            this.afkTimeout = -1;
            this.iconType = -1;
            this.iconFile = null;
            this.iconUrl = "";
            this.owner = null;
            this.splashType = -1;
            this.splashFile = null;
            this.splashUrl = "";
            this.discoverySplashType = -1;
            this.discoverySplashFile = null;
            this.discoverySplashUrl = "";
            this.bannerType = -1;
            this.bannerFile = null;
            this.bannerUrl = "";
            this.systemChannel = null;
            this.systemChannelFlags = null;
            this.rulesChannel = null;
            this.updatesChannel = null;
            this.locale = "";
            this.features = null;
            this.description = "";
            this.boostBarEnabled = false;
            this.splash = null;
            this.discoverySplash = null;
            this.icon = null;
            this.banner = null;
        }

        public Updater setBoostBarEnabled(boolean boostBarEnabled) {
            this.boostBarEnabled = boostBarEnabled;
            return this;
        }

        public Updater setDescription(String description) {
            this.description = description;
            return this;
        }

        public Updater addFeaturesToDefault(Set<GuildFeature> features) {
            this.features = guild.getFeatures();
            this.features.addAll(features);
            return this;
        }

        public Updater setFeatures(Set<GuildFeature> features) {
            this.features = features;
            return this;
        }

        public Updater setLanguage(DiscordLanguage language) {
            this.locale = language.getId();
            return this;
        }

        public Updater setUpdatesChannel(MessageableChannel channel) {
            this.updatesChannel = channel;
            return this;
        }

        public Updater setRulesChannel(MessageableChannel channel) {
            this.rulesChannel = channel;
            return this;
        }

        public Updater setSystemChannelFlags(Set<SystemChannelFlag> systemChannelFlags) {
            this.systemChannelFlags = systemChannelFlags;
            return this;
        }

        public Updater setSystemChannel(BaseChannel systemChannel) {
            this.systemChannel = systemChannel;
            return this;
        }

        public Updater setOwner(Member owner) {
            this.owner = owner;
            return this;
        }

        public Updater setAfkTimeout(int afkTimeout) {
            this.afkTimeout = afkTimeout;
            return this;
        }

        public Updater setAfkChannel(VoiceChannel afkChannel) {
            this.afkChannel = afkChannel;
            return this;
        }

        public Updater setExplicitContentLevel(ExplicitContentFilterLevel explicitContentFilterLevel) {
            int t = 0;
            switch (explicitContentFilterLevel) {
                case DISABLED -> t = 0;
                case ALL_MEMBERS -> t = 2;
                case MEMBERS_WITHOUT_ROLES -> t = 1;
            }

            this.explicitLevel = t;
            return this;
        }

        public Updater setDefaultNotificationLevel(DefaultNotificationLevel defaultNotificationLevel) {
            int t = 0;
            switch (defaultNotificationLevel) {
                case ALL_MESSAGES -> t = 0;
                case ONLY_MENTIONS -> t = 1;
            }

            this.defaultNotifyLevel = t;
            return this;
        }

        public Updater setVerificationLevel(VerificationLevel level) {
            int t = 0;

            switch (level) {
                case NONE -> t = 0;
                case LOW -> t = 1;
                case MEDIUM -> t = 2;
                case HIGH -> t = 3;
                case VERY_HIGH -> t = 4;
            }

            this.verifyLevel = t;
            return this;
        }

        public Updater setBanner(String url, ImageType type) {
            this.bannerType = 1;
            this.bannerUrl = url;
            this.banner = type;
            return this;
        }

        public Updater setBanner(File file, ImageType type) {
            this.bannerType = 0;
            this.bannerFile = file;
            this.banner = type;
            return this;
        }

        public Updater setDiscoverySplash(String url, ImageType type) {
            this.discoverySplashUrl = url;
            this.discoverySplashType = 1;
            this.discoverySplash = type;
            return this;
        }

        public Updater setDiscoverySplash(File file, ImageType type) {
            this.discoverySplash = type;
            this.discoverySplashType = 0;
            this.discoverySplashFile = file;
            return this;
        }

        public Updater setSplash(String url, ImageType type) {
            this.splash = type;
            this.splashType = 1;
            this.splashUrl = url;
            return this;
        }

        public Updater setSplash(File file, ImageType type) {
            this.splash = type;
            this.splashType = 0;
            this.splashFile = file;
            return this;
        }

        public Updater setIcon(String url, ImageType type) {
            this.iconType = 1;
            this.iconUrl = url;
            this.icon = type;
            return this;
        }

        public Updater setIcon(File file, ImageType type) {
            this.iconType = 0;
            this.iconFile = file;
            this.icon = type;
            return this;
        }

        public Updater setName(String name) {
            this.name = name;
            return this;
        }

        public void update() {
            ObjectNode obj = Constants.MAPPER.createObjectNode();

            if(!name.isEmpty()) {
                obj.put("name", name);
            }

            if(verifyLevel != -1) {
                obj.put("verification_level", verifyLevel);
            }

            if(defaultNotifyLevel != -1) {
                obj.put("default_message_notifications", defaultNotifyLevel);
            }

            if(explicitLevel != -1) {
                obj.put("explicit_content_filter", explicitLevel);
            }

            if(afkChannel != null) {
                obj.put("afk_channel_id", afkChannel.getId());
            }

            if(afkTimeout != -1) {
                obj.put("afk_timeout", afkTimeout);
            }

            if(iconType != -1) {
                if(iconType == 0) {
                    obj.put("icon", ImageUtils.toDataString(iconFile, icon));
                } else if(iconType == 1) {
                    obj.put("icon", ImageUtils.toDataString(iconUrl, icon));
                }
            }

            if(splashType != -1) {
                if(splashType == 0) {
                    obj.put("splash", ImageUtils.toDataString(splashFile, splash));
                } else if(splashType == 1) {
                    obj.put("splash", ImageUtils.toDataString(splashUrl, splash));
                }
            }

            if(discoverySplashType != -1) {
                if(discoverySplashType == 0) {
                    obj.put("discovery_splash", ImageUtils.toDataString(discoverySplashFile, discoverySplash));
                } else if(discoverySplashType == 1) {
                    obj.put("discovery_splash", ImageUtils.toDataString(discoverySplashUrl, discoverySplash));
                }
            }

            if(bannerType != -1) {
                if(bannerType == 0) {
                    obj.put("banner", ImageUtils.toDataString(bannerFile, banner));
                } else if(bannerType == 1) {
                    obj.put("banner", ImageUtils.toDataString(bannerUrl, banner));
                }
            }

            if(owner != null) {
                obj.put("owner_id", owner.getUser().getId());
            }

            if(systemChannel != null) {
                obj.put("system_channel_id", systemChannel.getId());
            }

            if(systemChannelFlags != null) {
                int code = 0;
                for(SystemChannelFlag flag: systemChannelFlags) {
                    code += flag.getCode();
                }

                obj.put("system_channel_flags", code);
            }

            if(rulesChannel != null) {
                obj.put("rules_channel_id", rulesChannel.getId());
            }

            if(updatesChannel != null) {
                obj.put("public_updates_channel_id", updatesChannel.getId());
            }

            if(!locale.isEmpty()) {
                obj.put("preferred_locale", locale);
            }

            if(features != null) {
                ArrayNode arr = Constants.MAPPER.createArrayNode();

                for(GuildFeature feature: features) {
                    arr.add(feature.name());
                }

                obj.set("features", arr);
            }

            if(!description.isEmpty()) {
                obj.put("description", description);
            }

            obj.put("premium_progress_bar_enabled", boostBarEnabled);

            Request request = new Request.Builder()
                    .url(Constants.BASE_URL + "/guilds/" + guild.getId())
                    .patch(RequestBody.create(obj.toString(), MediaType.parse("application/json")))
                    .build();

            try(Response resp = guild.getClient().getHttpClient().newCall(request).execute()) {
                String res = resp.body().string();
                ErrHandler.handle(res);
            } catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    public DiscordClient getClient() {
        return client;
    }

    public GuildPreview getPreview() {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + id + "/preview")
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return GuildPreview.fromJson(client, Constants.MAPPER.readTree(res));
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static boolean exists(JsonNode json, String name) {
        if(json.get(name) != null) {
            return !json.get(name).isNull();
        }
        return false;
    }

    public static Guild fromJson(DiscordClient client, JsonNode json) {
        String id = "";

        if(json.get("id") != null) {
            id = json.get("id").asText();
        }

        String name = "";

        if(json.get("name") != null) {
            name = json.get("name").asText();
        }

        String icon = "";

        if(exists(json, "icon")) {
            icon = json.get("icon").asText();
        }

        String splash = "";
        if(exists(json, "splash")) {
            splash = json.get("splash").asText();
        }

        String discoverySplash = "";
        if(exists(json, "discovery_splash")) {
            discoverySplash = json.get("discovery_splash").asText();
        }

        String ownerId = "";
        if(exists(json, "owner_id")) {
            ownerId = json.get("owner_id").asText();
        }

        VoiceChannel afkChannel = null;
        int afkTimeout = 0;

        if(exists(json, "afk_channel_id")) {
            afkChannel = client.getChannelById(json.get("afk_channel_id").asText()).get().asVoiceChannel();
        }

        if(exists(json, "afk_timeout")) {
            afkTimeout = json.get("afk_timeout").asInt();
        }

        boolean widgetEnabled = false;
        if(exists(json, "widget_enabled")) {
            widgetEnabled = json.get("widget_enabled").asBoolean();
        }

        BaseChannel widgetChannel = null;
        if(exists(json, "widget_channel_id")) {
            widgetChannel = client.getCacheManager().getChannelCache().getUnchecked(json.get("widget_channel_id").asText());
        }

        VerificationLevel verificationLevel = VerificationLevel.NONE;
        if(exists(json, "verification_level")) {
            int t = json.get("verification_level").asInt();

            switch (t) {
                case 0 -> verificationLevel = VerificationLevel.NONE;
                case 1 -> verificationLevel = VerificationLevel.LOW;
                case 2 -> verificationLevel = VerificationLevel.MEDIUM;
                case 3 -> verificationLevel = VerificationLevel.HIGH;
                case 4 -> verificationLevel = VerificationLevel.VERY_HIGH;
            }
        }

        DefaultNotificationLevel defaultNotificationLevel = DefaultNotificationLevel.ALL_MESSAGES;
        if(exists(json, "default_message_notifications")) {
            int t = json.get("default_message_notifications").asInt();

            if(t == 1) {
                defaultNotificationLevel = DefaultNotificationLevel.ONLY_MENTIONS;
            }
        }

        ExplicitContentFilterLevel explicitContentFilterLevel = ExplicitContentFilterLevel.DISABLED;
        if(exists(json, "explicit_content_filter")) {
            int t = json.get("explicit_content_filter").asInt();

            switch (t) {
                case 0 -> explicitContentFilterLevel = ExplicitContentFilterLevel.DISABLED;
                case 1 -> explicitContentFilterLevel = ExplicitContentFilterLevel.MEMBERS_WITHOUT_ROLES;
                case 2 -> explicitContentFilterLevel = ExplicitContentFilterLevel.ALL_MEMBERS;
            }
        }

        Set<Role> roles = new HashSet<>();
        Set<Emoji> emojis = new HashSet<>();
        if(exists(json, "roles")) {
            for(JsonNode el: json.get("roles")) {
                roles.add(Role.fromJson(client, el));
            }
        }

        if(exists(json, "emojis")) {
            for(JsonNode el: json.get("emojis")) {
                emojis.add(Emoji.fromJson(client, el));
            }
        }

        Set<GuildFeature> features = new HashSet<>();
        if(exists(json, "features")) {
            for(JsonNode el: json.get("features")) {
                for(GuildFeature feature: GuildFeature.values()) {
                    if(feature.name().toLowerCase().equals(el.asText().toLowerCase())) {
                        features.add(feature);
                    }
                }
            }
        }

        MfaLevel mfaLevel = MfaLevel.NONE;
        if(exists(json, "mfa_level")) {
            int t = json.get("mfa_level").asInt();

            if(t == 1) {
                mfaLevel = MfaLevel.ELEVATED;
            }
        }

        BaseChannel systemChannel = null;
        if(exists(json, "system_channel_id")) {
            systemChannel = client.getCacheManager().getChannelCache().getUnchecked(json.get("system_channel_id").asText());
        }

        Set<SystemChannelFlag> systemChannelFlags = new HashSet<>();
        if(exists(json, "system_channel_flags")) {
            int code = json.get("system_channel_flags").asInt();

            for(SystemChannelFlag flag: SystemChannelFlag.values()) {
                if((flag.getCode() & code) == flag.getCode()) {
                    systemChannelFlags.add(flag);
                }
            }
        }

        MessageableChannel rulesChannel = null;
        if(exists(json, "rules_channel_id")) {
            rulesChannel = client.getCacheManager().getChannelCache().getUnchecked(json.get("rules_channel_id").asText()).asMessageable();
        }

        int maxMembers = 0;
        if(exists(json, "max_members")) {
            maxMembers = json.get("max_members").asInt();
        }

        String vanityCode = "";
        if(exists(json, "vanity_url_code")) {
            vanityCode = json.get("vanity_url_code").asText();
        }

        String description = "";
        if(exists(json, "description")) {
            description = json.get("description").asText();
        }

        String banner = "";
        if(exists(json, "banner")) {
            banner = json.get("banner").asText();
        }

        GuildBoostTier boostTier = GuildBoostTier.NONE;
        if(exists(json, "premium_tier")) {
            int t = json.get("premium_tier").asInt();

            switch (t) {
                case 1 -> boostTier = GuildBoostTier.TIER_1;
                case 2 -> boostTier = GuildBoostTier.TIER_2;
                case 3 -> boostTier = GuildBoostTier.TIER_3;
            }
        }

        int boostCount = 0;
        if(exists(json, "premium_subscription_count")) {
            boostCount = json.get("premium_subscription_count").asInt();
        }

        DiscordLanguage preferredLang = null;
        if(exists(json, "preferred_locale")) {
            String e = json.get("preferred_locale").asText();

            for(DiscordLanguage language: DiscordLanguage.values()) {
                if(language.getId().equals(e)) {
                    preferredLang = language;
                    break;
                }
            }
        }

        MessageableChannel updatesChannel = null;
        if(exists(json, "public_updates_channel_id")) {
            updatesChannel = client.getCacheManager().getChannelCache().getUnchecked(json.get("public_updates_channel_id").asText()).asMessageable();
        }

        int maxVideoUsers = 0;
        if(exists(json, "max_video_channel_users")) {
            maxVideoUsers = json.get("max_video_channel_users").asInt();
        }

        int maxStageVideoUsers = 0;
        if(exists(json, "max_stage_video_channel_users")) {
            maxStageVideoUsers = json.get("max_stage_video_channel_users").asInt();
        }

        int memberCount = 0;
        if(exists(json, "approximate_member_count")) {
            memberCount = json.get("approximate_member_count").asInt();
        }

        int presenceCount = 0;
        if(exists(json, "approximate_presence_count")) {
            presenceCount = json.get("approximate_presence_count").asInt();
        }

        GuildWelcomeScreen welcomeScreen = null;
        if(exists(json, "welcome_screen")) {
            welcomeScreen = GuildWelcomeScreen.fromJson(client, json.get("welcome_screen"));
        }

        NsfwLevel nsfwLevel = NsfwLevel.DEFAULT;
        if(exists(json, "nsfw_level")) {
            int t = json.get("nsfw_level").asInt();

            switch (t) {
                case 1 -> nsfwLevel = NsfwLevel.EXPLICIT;
                case 2 -> nsfwLevel = NsfwLevel.SAFE;
                case 3 -> nsfwLevel = NsfwLevel.AGE_RESTRICTED;
            }
        }

        Set<Sticker> stickers = new HashSet<>();
        if(exists(json, "stickers")) {
            for(JsonNode el: json.get("stickers")) {
                stickers.add(Sticker.fromJson(el));
            }
        }

        boolean boostsBarEnabled = false;
        if(exists(json, "premium_progress_bar_enabled")) {
            boostsBarEnabled = json.get("premium_progress_bar_enabled").asBoolean();
        }

        return new Guild(client, id, name, icon, splash, discoverySplash, ownerId, afkChannel, afkTimeout, widgetEnabled, widgetChannel, verificationLevel, defaultNotificationLevel, explicitContentFilterLevel, roles, emojis, features, mfaLevel, systemChannel, systemChannelFlags, rulesChannel, maxMembers, vanityCode, description, banner, boostTier, boostCount, preferredLang, updatesChannel, maxVideoUsers, maxStageVideoUsers, memberCount, presenceCount, welcomeScreen, nsfwLevel, stickers, boostsBarEnabled);
    }

    public Member getMemberById(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + this.id + "/members/" + id)
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Member.fromJson(client, Constants.MAPPER.readTree(res), this.id);
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public Member getOwner() {
        return owner;
    }

    public String getName() {
        return name;
    }

    /**
     * @apiNote Default image type is png**/
    public String getIconUrl() {
        return Constants.BASE_IMAGES_URL + "icons/" + id + "/" + iconHash + ".png";
    }

    public String getIconUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "icons/" + id + "/" + iconHash + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    public String getIconHash() {
        return iconHash;
    }

    /**
     * @apiNote Default image type is png**/
    public String getSplashUrl() {
        return Constants.BASE_IMAGES_URL + "splashes/" + id + "/" + splash + ".png";
    }

    public String getSplashUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "splashes/" + id + "/" + splash + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    public String getSplashHash() {
        return splash;
    }

    public String getDiscoverySplashUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "discovery-splashes/" + id + "/" + discoverySplash + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    /**
     * @apiNote Default image type is png**/
    public String getDiscoverySplashUrl() {
        return Constants.BASE_IMAGES_URL + "discovery-splashes/" + id + "/" + discoverySplash + ".png";
    }

    public String getDiscoverySplashHash() {
        return discoverySplash;
    }

    public VoiceChannel getAfkChannel() {
        return afkChannel;
    }

    public int getAfkTimeout() {
        return afkTimeout;
    }

    public boolean isWidgetEnabled() {
        return widgetEnabled;
    }

    public BaseChannel getWidgetChannel() {
        return widgetChannel;
    }

    public VerificationLevel getVerificationLevel() {
        return verificationLevel;
    }

    public DefaultNotificationLevel getDefaultNotificationLevel() {
        return defaultNotificationLevel;
    }

    public ExplicitContentFilterLevel getExplicitContentFilterLevel() {
        return explicitContentFilterLevel;
    }

    public ImmutableSet<Role> getRoles() {
        return ImmutableSet.copyOf(roles);
    }

    public ImmutableSet<Emoji> getEmojis() {
        return ImmutableSet.copyOf(emojis);
    }

    public Set<GuildFeature> getFeatures() {
        return features;
    }

    public MfaLevel getMfaLevel() {
        return mfaLevel;
    }

    public BaseChannel getSystemChannel() {
        return systemChannel;
    }

    public ImmutableSet<SystemChannelFlag> getSystemChannelFlags() {
        return ImmutableSet.copyOf(systemChannelFlags);
    }

    public BaseChannel getRulesChannel() {
        return rulesChannel;
    }

    public int getMaxMembers() {
        return maxMembers;
    }

    public String getVanityUrlCode() {
        return vanityCode;
    }

    public String getDescription() {
        return description;
    }

    public String getBannerUrl(ImageType type) {
        return Constants.BASE_IMAGES_URL + "banners/" + id + "/" + banner + (type == ImageType.GIF ? ".gif" : (type == ImageType.PNG ? ".png" : (type == ImageType.JPEG ? ".jpg" : ".webp")));
    }

    /**
     * @apiNote Default image type is png**/
    public String getBannerUrl() {
        return Constants.BASE_IMAGES_URL + "banners/" + id + "/" + banner + ".png";
    }

    public String getBannerHash() {
        return banner;
    }

    public GuildBoostTier getBoostTier() {
        return boostTier;
    }

    public int getBoostCount() {
        return boostCount;
    }

    public DiscordLanguage getPreferredLang() {
        return preferredLang;
    }

    public MessageableChannel getUpdatesChannel() {
        return updatesChannel;
    }

    public int getMaxVideoUsers() {
        return maxVideoUsers;
    }

    public int getMaxStageVideoUsers() {
        return maxStageVideoUsers;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public int getPresenceCount() {
        return presenceCount;
    }

    public GuildWelcomeScreen getWelcomeScreen() {
        return welcomeScreen;
    }

    public NsfwLevel getNsfwLevel() {
        return nsfwLevel;
    }

    public ImmutableSet<Sticker> getStickers() {
        return ImmutableSet.copyOf(stickers);
    }

    public boolean isBoostsBarEnabled() {
        return boostsBarEnabled;
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
