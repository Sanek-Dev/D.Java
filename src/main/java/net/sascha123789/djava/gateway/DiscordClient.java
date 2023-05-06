/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.cache.LoadingCache;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.Attachment;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.guild.Guild;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.enums.SlashCommandOptionType;
import net.sascha123789.djava.api.interactions.slash.EnteredOption;
import net.sascha123789.djava.api.interactions.slash.SlashCommandUseEvent;
import net.sascha123789.djava.api.managers.CacheManager;
import net.sascha123789.djava.gateway.events.*;
import net.sascha123789.djava.gateway.intents.DiscordIntent;
import net.sascha123789.djava.gateway.intents.DiscordIntents;
import net.sascha123789.djava.gateway.presence.Activity;
import net.sascha123789.djava.gateway.presence.ActivityType;
import net.sascha123789.djava.gateway.presence.DiscordStatus;
import net.sascha123789.djava.utils.ChannelUtils;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.DiscordAPIException;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;
import org.apache.commons.lang3.SystemUtils;
import org.checkerframework.checker.units.qual.A;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordClient {
    private static final Map<String, DiscordClient> clients = new HashMap<>();
    private static final Map<String, String> uuids = new HashMap<>();
    private static Thread keepAliveThread;
    private static final CountDownLatch keepAliveLatch = new CountDownLatch(1);
    private static boolean keepReady;
    private boolean optimized;

    private static void init() {
        keepAliveThread = new Thread(() -> {
            try {
                keepAliveLatch.await();
            } catch(Exception e) {
                e.printStackTrace();
            }
        });

        keepAliveThread.start();
    }

    public static String getUuidById(String id) {
        for(Map.Entry<String, String> entry: uuids.entrySet()) {
            if(entry.getKey().equals(id)) return entry.getValue();
        }

        return null;
    }

    public static Optional<DiscordClient> getClientByUuid(String uuid) {
        for(Map.Entry<String, DiscordClient> entry: clients.entrySet()) {
            if(entry.getKey().equals(uuid)) return Optional.of(entry.getValue());
        }

        return Optional.empty();
    }

    @Override
    public String toString() {
        return "DiscordClient[uuid=" + this.uuid + ",token=" + token + "]";
    }

    public static boolean clientExists(DiscordClient client) {
        for(Map.Entry<String, DiscordClient> entry: clients.entrySet()) {
            if(entry.getValue().equals(client)) return true;
        }

        return false;
    }

    private final String uuid;
    private String token;
    private String gatewayUrl;
    private int recommendedShards;
    private OkHttpClient httpClient;
    private WebSocketClient socket;
    private final Set<EventAdapter> adapters;
    private int heartbeatInterval;
    private boolean debug;
    private List<DiscordIntent> intents;
    private List<Activity> activities;
    private DiscordStatus status;
    private boolean sharding;
    private int shardCount;
    private boolean useRecommendedShardCount;
    private String sessionId;
    private String resumeUrl;
    private int apiVersion;
    private String appId;
    private int lastSeq;
    private boolean running;
    private SelfUser selfUser;
    private CacheManager cacheManager;
    private List<Guild> guilds;

    public boolean isRunning() {
        return running;
    }

    public int getLastSeq() {
        return lastSeq;
    }

    public String getApplicationId() {
        return appId;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public String getSessionId() {
        return sessionId;
    }

    public List<Activity> getActivities()
    {
        return activities;
    }

    public DiscordStatus getStatus() {
        return status;
    }

    public boolean isDebug() {
        return debug;
    }

    public String getToken() {
        return token;
    }

    public String getGatewayUrl() {
        return gatewayUrl;
    }

    public int getRecommendedShardCount() {
        return recommendedShards;
    }

    public Set<EventAdapter> getEventAdapters() {
        return adapters;
    }

    public int getHeartbeatInterval() {
        return heartbeatInterval;
    }

    public WebSocketClient getSocket() {
        return socket;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) return false;

        if(obj.getClass() != this.getClass()) return false;

        DiscordClient o = (DiscordClient) obj;

        return o.uuid.equals(this.uuid);
    }

    public List<Guild> getGuilds() {
        return guilds;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    /**
     * @return Unique identifier of this client instance**/
    public String getUuid() {
        return uuid;
    }

    public static class Builder {
        private String token;
        private boolean debug;
        private List<DiscordIntent> intents;
        private List<Activity> activities;
        private DiscordStatus status;
        private boolean sharding;
        private int shardCount;
        private boolean useRecommendedShardCount;
        private Set<EventAdapter> adapters;
        private boolean optimized;

        public Builder(String token) {
            this.token = token;
            this.debug = false;
            this.intents = DiscordIntents.getNonPrivilegedIntents();
            this.activities = new ArrayList<>();
            this.status = DiscordStatus.ONLINE;
            this.sharding = false;
            this.shardCount = 0;
            this.useRecommendedShardCount = false;
            this.adapters = new HashSet<>();
            this.optimized = false;
        }

        public Builder setOpimizedGc(boolean optimized) {
            this.optimized = optimized;
            return this;
        }

        public Builder addEventAdapter(EventAdapter adapter) {
            this.adapters.add(adapter);
            return this;
        }

        public Builder setToken(String token) {
            this.token = token;
            return this;
        }

        public Builder setDebug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder setIntents(DiscordIntent... intents) {
            this.intents = List.of(intents);
            return this;
        }

        public Builder setIntents(List<DiscordIntent> intents) {
            this.intents = intents;
            return this;
        }

        public Builder addIntents(DiscordIntent... intents) {
            this.intents.addAll(List.of(intents));
            return this;
        }

        public Builder addIntents(List<DiscordIntent> intents) {
            this.intents.addAll(intents);
            return this;
        }

        public Builder setActivities(Activity... activities) {
            this.activities = List.of(activities);
            return this;
        }

        public Builder setActivities(List<Activity> activities) {
            this.activities = activities;
            return this;
        }

        public Builder addActivity(Activity activity) {
            this.activities.add(activity);
            return this;
        }

        public Builder addActivities(Activity... activities) {
            this.activities.addAll(List.of(activities));
            return this;
        }

        public Builder addActivities(List<Activity> activities) {
            this.activities.addAll(activities);
            return this;
        }

        public Builder setStatus(DiscordStatus status) {
            this.status = status;
            return this;
        }

        public Builder setShardCount(int shardCount) {
            this.sharding = true;
            this.shardCount = shardCount;
            this.useRecommendedShardCount = false;
            return this;
        }

        public Builder setRecommendedShardCount() {
            this.sharding = true;
            this.useRecommendedShardCount = true;
            this.shardCount = 0;
            return this;
        }

        /**
         * @return Built DiscordClient**/
        public DiscordClient build() {
            return new DiscordClient(token, debug, intents, activities, status, sharding, shardCount, useRecommendedShardCount, adapters, optimized);
        }
    }

    /**
     * @param token Bot token
     * @param intents Bot intents**/
    public DiscordClient(String token, List<DiscordIntent> intents, Set<EventAdapter> adapters) {
        this.token = token;
        this.uuid = UUID.randomUUID().toString();

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            this.httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request();
                            Request newRequest = request.newBuilder()
                                    .addHeader("User-Agent", Constants.USER_AGENT)
                                    .addHeader("Authorization", "Bot " + token).build();
                            return chain.proceed(newRequest);
                        }
                    }).build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.adapters = adapters;
        this.debug = false;
        this.activities = new ArrayList<>();
        this.status = DiscordStatus.ONLINE;
        this.sharding = false;
        this.shardCount = 0;
        this.useRecommendedShardCount = false;
        this.running = false;

        Request getGateway = new Request.Builder()
                .url(Constants.BASE_URL + "/gateway/bot")
                .get()
                .build();

        StringBuilder getGatewayRes = new StringBuilder();

        try(Response resp = httpClient.newCall(getGateway).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
            getGatewayRes.append(str);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            JsonNode gatewayObj = Constants.MAPPER.readTree(getGatewayRes.toString());

            this.gatewayUrl = gatewayObj.get("url").asText() + "?v=10&encoding=json";
            this.recommendedShards = gatewayObj.get("shards").asInt();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.intents = intents;
        this.optimized = false;

        if(!keepReady) {
            init();
            keepReady = true;
        }

        clients.put(uuid, this);

        this.cacheManager = new CacheManager(this);
        this.guilds = new ArrayList<>();
    }

    /**
     * @param token Bot token
     * @param debugLogging Debug logging enabled
     * @param intents Bot intents**/
    public DiscordClient(String token, boolean debugLogging, List<DiscordIntent> intents, Set<EventAdapter> adapters) {
        this.token = token;
        this.uuid = UUID.randomUUID().toString();

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            this.httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request();
                            Request newRequest = request.newBuilder()
                                    .addHeader("User-Agent", Constants.USER_AGENT)
                                    .addHeader("Authorization", "Bot " + token).build();
                            return chain.proceed(newRequest);
                        }
                    }).build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.adapters = adapters;
        this.debug = debugLogging;
        this.activities = new ArrayList<>();
        this.status = DiscordStatus.ONLINE;
        this.sharding = false;
        this.shardCount = 0;
        this.useRecommendedShardCount = false;
        this.running = false;

        Request getGateway = new Request.Builder()
                .url(Constants.BASE_URL + "/gateway/bot")
                .get()
                .build();

        StringBuilder getGatewayRes = new StringBuilder();

        try(Response resp = httpClient.newCall(getGateway).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
            getGatewayRes.append(str);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            JsonNode gatewayObj = Constants.MAPPER.readTree(getGatewayRes.toString());

            this.gatewayUrl = gatewayObj.get("url").asText() + "?v=10&encoding=json";
            this.recommendedShards = gatewayObj.get("shards").asInt();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.intents = intents;
        this.optimized = false;

        if(!keepReady) {
            init();
            keepReady = true;
        }

        clients.put(uuid, this);

        DiscordClient self = this;
        this.cacheManager = new CacheManager(this);
        this.guilds = new ArrayList<>();
    }

    /**
     * @param token Bot token
     * @param debugLogging Debug logging enabled
     * @param intents Bot intents
     * @param status Bot status
     * @param activities Bot activities**/
    public DiscordClient(String token, boolean debugLogging, List<DiscordIntent> intents, List<Activity> activities, DiscordStatus status, boolean sharding, int shardCount, boolean useRecommendedShardCount, Set<EventAdapter> adapters, boolean optimized) {
        this.token = token;
        this.uuid = UUID.randomUUID().toString();

        try {
            TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {
                        @Override
                        public void checkClientTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public void checkServerTrusted(java.security.cert.X509Certificate[] chain, String authType) {
                        }

                        @Override
                        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                            return new java.security.cert.X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new java.security.SecureRandom());

            this.httpClient = new OkHttpClient.Builder()
                    .sslSocketFactory(sslContext.getSocketFactory(), (X509TrustManager) trustAllCerts[0])
                    .hostnameVerifier((hostname, session) -> true)
                    .connectTimeout(5, TimeUnit.SECONDS)
                    .addInterceptor(new Interceptor() {
                        @NotNull
                        @Override
                        public Response intercept(@NotNull Chain chain) throws IOException {
                            Request request = chain.request();
                            Request newRequest = request.newBuilder()
                                    .addHeader("User-Agent", Constants.USER_AGENT)
                                    .addHeader("Authorization", "Bot " + token).build();
                            return chain.proceed(newRequest);
                        }
                    }).build();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.adapters = adapters;
        this.debug = debugLogging;
        this.activities = activities;
        this.status = status;
        this.sharding = sharding;
        this.shardCount = shardCount;
        this.useRecommendedShardCount = useRecommendedShardCount;
        this.running = false;

        Request getGateway = new Request.Builder()
                .url(Constants.BASE_URL + "/gateway/bot")
                .get()
                .build();

        StringBuilder getGatewayRes = new StringBuilder();

        try(Response resp = httpClient.newCall(getGateway).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
            getGatewayRes.append(str);
        } catch(Exception e) {
            e.printStackTrace();
        }

        try {
            JsonNode gatewayObj = Constants.MAPPER.readTree(getGatewayRes.toString());

            this.gatewayUrl = gatewayObj.get("url").asText() + "?v=10&encoding=json";
            this.recommendedShards = gatewayObj.get("shards").asInt();
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.intents = intents;
        this.optimized = optimized;

        if(!keepReady) {
            init();
            keepReady = true;
        }

        clients.put(uuid, this);

        DiscordClient self = this;
        this.cacheManager = new CacheManager(this);
        this.guilds = new ArrayList<>();
    }

    public CacheManager getCacheManager() {
        return cacheManager;
    }

    public void setOptimizedGc(boolean optimized) {
        this.optimized = optimized;
    }

    public boolean isOptimized() {
        return optimized;
    }

    /**
     * @return Provided intents**/
    public List<DiscordIntent> getIntents() {
        return intents;
    }

    /**
     * Add a new Event Adapter
     * @return Current instance**/
    public void addEventAdapter(EventAdapter adapter) {
        this.adapters.add(adapter);
    }

    public void removeEventAdapter(EventAdapter adapter) {
        this.adapters.remove(adapter);
    }

    public OkHttpClient getHttpClient() {
        return httpClient;
    }

    private void identify() {
        ObjectNode heartbeat = Constants.MAPPER.createObjectNode();
        heartbeat.put("op", 1);
        heartbeat.putNull("d");

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
        scheduler.scheduleAtFixedRate(() -> {
            try {
                socket.send(Constants.MAPPER.writeValueAsString(heartbeat));
            } catch(Exception e) {
                e.printStackTrace();
            }

            for(EventAdapter adapter: adapters) {
                adapter.onHeartbeat(new HeartbeatEvent(this, heartbeatInterval));
            }
        }, 1000, (heartbeatInterval - 2000), TimeUnit.MILLISECONDS);

        for(EventAdapter adapter: adapters) {
            adapter.onHello(new HelloEvent(this, heartbeatInterval));
        }

        /* Identify */

        if(debug) {
            System.out.println("[D.Java]: Identifying gateway client...");
        }

        ObjectNode identify = Constants.MAPPER.createObjectNode();
        identify.put("token", token);

        ObjectNode properties = Constants.MAPPER.createObjectNode();
        properties.put("os", SystemUtils.OS_NAME);
        properties.put("browser", "D.Java");
        properties.put("device", "D.Java");

        identify.set("properties", properties);

        int[] shards;

        if(sharding) {
            if(useRecommendedShardCount) {
                shards = new int[]{0, recommendedShards};
            } else {
                shards = new int[]{0, shardCount};
            }

            ArrayNode arr = Constants.MAPPER.createArrayNode();
            arr.add(shards[0]);
            arr.add(shards[1]);

            identify.set("shard", arr);
        }

        ObjectNode presence = Constants.MAPPER.createObjectNode();
        presence.putNull("since");
        presence.put("status", (status == DiscordStatus.DO_NOT_DISTURB ? "dnd" : (status == DiscordStatus.OFFLINE ? "offline" : (status == DiscordStatus.IDLE ? "idle" : "online"))));
        presence.put("afk", false);

        if(!activities.isEmpty()) {
            ArrayNode arr = Constants.MAPPER.createArrayNode();

            for(Activity activity: activities) {
                ObjectNode o = Constants.MAPPER.createObjectNode();
                o.put("name", activity.getName());
                o.put("type", (activity.getType() == ActivityType.COMPETING ? 5 : (activity.getType() == ActivityType.LISTENING ? 2 : (activity.getType() == ActivityType.PLAYING ? 0 : (activity.getType() == ActivityType.STREAMING ? 1 : 3)))));

                if(!activity.getUrl().isEmpty()) {
                    o.put("url", activity.getUrl());
                }

                if(!activity.getDetails().isEmpty()) {
                    o.put("details", activity.getDetails());
                }

                if(!activity.getState().isEmpty()) {
                    o.put("state", activity.getState());
                }

                arr.add(o);
            }

            presence.set("activities", arr);
        } else {
            presence.set("activities", Constants.MAPPER.createArrayNode());
        }

        identify.set("presence", presence);
        long code = 0L;

        for(DiscordIntent intent: intents) {
            code += intent.getCode();
        }

        identify.put("intents", code);

        ObjectNode identifyEvent = Constants.MAPPER.createObjectNode();
        identifyEvent.set("d", identify);
        identifyEvent.put("op", 2);

        String pack = "";

        try {
            pack = Constants.MAPPER.writeValueAsString(identifyEvent);
        } catch(Exception e) {
            e.printStackTrace();
        }

        if(debug) {
            System.out.println("[D.Java]: Sending Identify packet:\n" + pack);
        }

        socket.send(pack);

        if(debug) {
            System.out.println("[D.Java]: Successfully identified gateway client!");
        }
    }

    private static void dispatchReady(DiscordClient client, JsonNode eventBody) {
        int v = eventBody.get("v").asInt();
        String sessionId = eventBody.get("session_id").asText();
        String resumeUrl = eventBody.get("resume_gateway_url").asText();
        String appId = eventBody.get("application").get("id").asText();
        client.apiVersion = v;
        client.sessionId = sessionId;
        client.resumeUrl = resumeUrl;
        client.appId = appId;
        client.running = true;
        client.selfUser = SelfUser.fromJson(client, eventBody.get("user"));

        for(EventAdapter adapter: client.adapters) {
            adapter.onReady(new ReadyEvent(client, v, sessionId, resumeUrl, appId, client.selfUser));
        }
    }

    private static void switchResolved(DiscordClient self, JsonNode data, SlashCommandOptionType typ, String guildId, Map<String, EnteredOption> options, JsonNode el) {
        JsonNode obj = data.get("resolved");

        if(obj.get("users") != null) {
            JsonNode users = obj.get("users");

            if(!users.isEmpty()) {
                EnteredOption o = new EnteredOption(typ, el.get("name").asText(), users.get(el.get("value").asText()), self, guildId);
                options.put(el.get("name").asText(), o);
            }
        }

        if(obj.get("roles") != null) {
            JsonNode roles = obj.get("roles");

            if(!roles.isEmpty()) {
                EnteredOption o = new EnteredOption(typ, el.get("name").asText(), roles.get(el.get("value").asText()), self, guildId);
                options.put(el.get("name").asText(), o);
            }
        }

        if(obj.get("channels") != null) {
            JsonNode channels = obj.get("channels");

            if(!channels.isEmpty()) {
                EnteredOption o = new EnteredOption(typ, el.get("name").asText(), channels.get(el.get("value").asText()), self, guildId);
                options.put(el.get("name").asText(), o);
            }
        }

        if(obj.get("attachments") != null) {
            JsonNode attachments = obj.get("attachments");

            if(!attachments.isEmpty()) {
                EnteredOption o = new EnteredOption(typ, el.get("name").asText(), attachments.get(el.get("value").asText()), self, guildId);
                options.put(el.get("name").asText(), o);
            }
        }
    }

    private static void dispatchInteractionCreate(DiscordClient self, JsonNode eventBody) {
        int type = eventBody.get("type").asInt();
        String id = eventBody.get("id").asText();
        String app = eventBody.get("application_id").asText();
        String token = eventBody.get("token").asText();
        String locale = eventBody.get("locale").asText();
        DiscordLanguage lang = null;

        for(DiscordLanguage language: DiscordLanguage.values()) {
            if(language.getId().equals(locale)) {
                lang = language;
                break;
            }
        }

        if(type == 2) {
            String channelId = "";

            if(eventBody.get("channel_id") != null) {
                if(!eventBody.get("channel_id").isNull()) {
                    channelId = eventBody.get("channel_id").asText();
                }
            }

            String guildId = "";

            if(eventBody.get("guild_id") != null) {
                if(!eventBody.get("guild_id").isNull()) {
                    guildId = eventBody.get("guild_id").asText();
                }
            }

            String guildLocale = "";

            if(eventBody.get("guild_locale") != null) {
                if(!eventBody.get("guild_locale").isNull()) {
                    guildLocale = eventBody.get("guild_locale").asText();
                }
            }

            DiscordLanguage l = null;

            for(DiscordLanguage el: DiscordLanguage.values()) {
                if(el.getId().equals(guildLocale)) {
                    l = el;
                    break;
                }
            }
            BaseChannel channel = null;

            if(eventBody.get("channel") != null) {
                if(!eventBody.get("channel").isNull()) {
                    channel = ChannelUtils.switchTypes(self, eventBody.get("channel"));
                }
            }
            JsonNode data = eventBody.get("data");
            String name = data.get("name").asText();
            String groupName = "";
            String subName = "";
            Map<String, EnteredOption> options = new HashMap<>();

            if(data.get("options") != null) {
                JsonNode arr = data.get("options");

                if(!arr.isEmpty()) {
                    for(JsonNode el: arr) {
                        int t = el.get("type").asInt();

                        if(t == 2) {
                            groupName = el.get("name").asText();

                            if(el.get("options") != null) {
                                JsonNode arrSub = el.get("options");

                                if(!arrSub.isEmpty()) {
                                    for(JsonNode el1: arrSub) {
                                        int t1 = el1.get("type").asInt();

                                        if(t1 == 1) {
                                            subName = el1.get("name").asText();

                                            if(el1.get("options") != null) {
                                                JsonNode arr1 = el1.get("options");

                                                if(!arr1.isEmpty()) {
                                                    for(JsonNode el2: arr1) {
                                                        int t2 = el2.get("type").asInt();
                                                        SlashCommandOptionType typ = (t2 == 3 ? SlashCommandOptionType.STRING : (t2 == 4 ? SlashCommandOptionType.INTEGER : (t2 == 5 ? SlashCommandOptionType.BOOLEAN : (t2 == 6 ? SlashCommandOptionType.USER : (t2 == 7 ? SlashCommandOptionType.CHANNEL : (t2 == 8 ? SlashCommandOptionType.ROLE : (t2 == 9 ? SlashCommandOptionType.MENTIONABLE : (t2 == 10 ? SlashCommandOptionType.NUMBER : SlashCommandOptionType.ATTACHMENT))))))));

                                                        if(typ == SlashCommandOptionType.USER || typ == SlashCommandOptionType.ROLE || typ == SlashCommandOptionType.CHANNEL || typ == SlashCommandOptionType.ATTACHMENT) {
                                                            switchResolved(self, data, typ, guildId, options, el2);
                                                        } else {
                                                            options.put(el2.get("name").asText(), new EnteredOption(typ, el2.get("name").asText(), el2.get("value"), self, guildId));
                                                        }
                                                    }
                                                }
                                            }
                                        } else {
                                            SlashCommandOptionType typ = (t1 == 3 ? SlashCommandOptionType.STRING : (t1 == 4 ? SlashCommandOptionType.INTEGER : (t1 == 5 ? SlashCommandOptionType.BOOLEAN : (t1 == 6 ? SlashCommandOptionType.USER : (t1 == 7 ? SlashCommandOptionType.CHANNEL : (t1 == 8 ? SlashCommandOptionType.ROLE : (t1 == 9 ? SlashCommandOptionType.MENTIONABLE : (t1 == 10 ? SlashCommandOptionType.NUMBER : SlashCommandOptionType.ATTACHMENT))))))));

                                            if(typ == SlashCommandOptionType.USER || typ == SlashCommandOptionType.ROLE || typ == SlashCommandOptionType.CHANNEL || typ == SlashCommandOptionType.ATTACHMENT) {
                                                switchResolved(self, data, typ, guildId, options, el1);
                                            } else {
                                                options.put(el1.get("name").asText(), new EnteredOption(typ, el1.get("name").asText(), el1.get("value"), self, guildId));
                                            }
                                        }
                                    }
                                }
                            }
                        } else if(t == 1) {
                            subName = el.get("name").asText();
                        } else {
                            SlashCommandOptionType typ = (t == 3 ? SlashCommandOptionType.STRING : (t == 4 ? SlashCommandOptionType.INTEGER : (t == 5 ? SlashCommandOptionType.BOOLEAN : (t == 6 ? SlashCommandOptionType.USER : (t == 7 ? SlashCommandOptionType.CHANNEL : (t == 8 ? SlashCommandOptionType.ROLE : (t == 9 ? SlashCommandOptionType.MENTIONABLE : (t == 10 ? SlashCommandOptionType.NUMBER : SlashCommandOptionType.ATTACHMENT))))))));
                            if(typ == SlashCommandOptionType.USER || typ == SlashCommandOptionType.ROLE || typ == SlashCommandOptionType.CHANNEL || typ == SlashCommandOptionType.ATTACHMENT) {
                                switchResolved(self, data, typ, guildId, options, el);
                            } else {
                                options.put(el.get("name").asText(), new EnteredOption(typ, el.get("name").asText(), el.get("value"), self, guildId));
                            }
                        }
                    }
                }
            }

            for(EventAdapter adapter: self.adapters) {
                adapter.onSlashCommandUse(new SlashCommandUseEvent(options, subName, groupName, name, channel, self, id, token, app, lang, channelId, guildId, l, Member.fromJson(self, eventBody.get("member"), guildId)));
            }
        }
    }

    private static void dispatchMessageDeleteBulk(DiscordClient self, JsonNode eventBody) {
        Set<String> ids = new HashSet<>();
        JsonNode arr = eventBody.get("ids");

        for(JsonNode el: arr) {
            ids.add(el.asText());
        }

        String guildId = "";
        if(eventBody.get("guild_id") != null) {
            if(!eventBody.get("guild_id").isNull()) {
                guildId = eventBody.get("guild_id").asText();
            }
        }

        for(EventAdapter adapter: self.adapters) {
            adapter.onMessageBulkDelete(new MessageDeleteBulkEvent(self, ids, eventBody.get("channel_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId)));
        }
    }

    private static void dispatchMessages(DiscordClient self, JsonNode eventBody, String name) {
        switch(name) {
            case "MESSAGE_CREATE" -> {
                String guildId = "";
                if (eventBody.get("guild_id") != null) {
                    if (!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                Guild guild = null;
                try {
                    guild = self.cacheManager.getGuildCache().get(guildId);
                } catch(Exception e) {
                    e.printStackTrace();
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageCreate(new MessageCreateEvent(self, Message.fromJson(self, eventBody), guild));
                }
            }
            case "MESSAGE_UPDATE" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageUpdate(new MessageUpdateEvent(self, Message.fromJson(self, eventBody), self.cacheManager.getGuildCache().getUnchecked(guildId)));
                }
            }
            case "MESSAGE_DELETE" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageDelete(new MessageDeleteEvent(self, eventBody.get("id").asText(), eventBody.get("channel_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId)));
                }
            }
            case "MESSAGE_DELETE_BULK" -> dispatchMessageDeleteBulk(self, eventBody);
            case "MESSAGE_REACTION_ADD" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageReactionAdd(new MessageReactionAddEvent(self, eventBody.get("user_id").asText(), eventBody.get("channel_id").asText(), eventBody.get("message_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId), Emoji.fromJson(self, eventBody.get("emoji"))));
                }
            }
            case "MESSAGE_REACTION_REMOVE" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageReactionRemove(new MessageReactionRemoveEvent(self, eventBody.get("user_id").asText(), eventBody.get("channel_id").asText(), eventBody.get("message_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId), Emoji.fromJson(self, eventBody.get("emoji"))));
                }
            }
            case "MESSAGE_REACTION_REMOVE_ALL" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageReactionRemoveAll(new MessageReactionRemoveAllEvent(self, eventBody.get("channel_id").asText(), eventBody.get("message_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId)));
                }
            }
            case "MESSAGE_REACTION_REMOVE_EMOJI" -> {
                String guildId = "";
                if(eventBody.get("guild_id") != null) {
                    if(!eventBody.get("guild_id").isNull()) {
                        guildId = eventBody.get("guild_id").asText();
                    }
                }

                for(EventAdapter adapter: self.adapters) {
                    adapter.onMessageReactionRemoveAllEmoji(new MessageReactionRemoveAllEmojiEvent(self, eventBody.get("channel_id").asText(), self.cacheManager.getGuildCache().getUnchecked(guildId), eventBody.get("message_id").asText(), Emoji.fromJson(self, eventBody.get("emoji"))));
                }
            }
        }
    }

    private static void dispatchGuilds(DiscordClient client, JsonNode eventBody, String name) {
        switch(name) {
            case "GUILD_CREATE" -> {
                String id = eventBody.get("id").asText();
                Guild guild = client.getGuildById(id).get();

                client.getCacheManager().getGuildCache().put(guild.getId(), guild);
                client.guilds.add(guild);



                if(client.isDebug()) {
                    System.out.println("[D.Java]: Loaded guild " + guild.getId() + " cache!");
                }
            }
            case "GUILD_UPDATE" -> {
                Guild instance = Guild.fromJson(client, eventBody);
                LoadingCache<String, Guild> cache = client.getCacheManager().getGuildCache();
                cache.put(instance.getId(), instance);
                client.guilds.remove(instance);
                client.guilds.add(instance);
            }
            case "GUILD_DELETE" -> {
                String id = eventBody.get("id").asText();

                try {
                    client.guilds.remove(client.getCacheManager().getGuildCache().get(id));
                } catch(Exception e) {
                    e.printStackTrace();
                }

                client.getCacheManager().getGuildCache().invalidate(id);
            }
        }
    }

    /**
     * Connect to the Discord Gateway and run bot
     * @throws DiscordAPIException On any Discord Gateway error and if client is already running*/
    public void run() {
        if(running) {
            throw new DiscordAPIException("DiscordClient already running!");
        }

        DiscordClient self = this;

        try {
            this.socket = new WebSocketClient(URI.create(gatewayUrl)) {
                @Override
                public void onOpen(ServerHandshake handshakeData) {
                    if(debug) {
                        System.out.println("[D.Java]: Successfully opened Discord Gateway connection!");
                    }
                }

                @Override
                public void onMessage(String message) {
                    if(debug) {
                        System.out.println("[D.Java]: Received Discord Gateway message:\n" + message);
                    }

                    JsonNode eventObj = null;

                    try {
                        eventObj = Constants.MAPPER.readTree(message);
                    } catch(Exception e) {
                        e.printStackTrace();
                    }

                    JsonNode eventBody = null;

                    if(!eventObj.get("d").isNull()) {
                        eventBody = eventObj.get("d");
                    }

                    int eventOp = 0;
                    String eventName = "";

                    if(!eventObj.get("op").isNull()) {
                        eventOp = eventObj.get("op").asInt();
                    }

                    if(!eventObj.get("t").isNull()) {
                        eventName = new String(eventObj.get("t").asText().getBytes(StandardCharsets.UTF_8));
                    }

                    if(!eventObj.get("s").isNull()) {
                        lastSeq = eventObj.get("s").asInt();
                    }

                    switch (eventOp) {
                        case 10 -> {
                            heartbeatInterval = eventBody.get("heartbeat_interval").asInt();
                            identify();
                        }
                        case 7 -> self.reconnect();
                    }

                    switch (eventName) {
                        case "READY" -> dispatchReady(self, eventBody);
                        case "INTERACTION_CREATE" -> dispatchInteractionCreate(self, eventBody);
                        default -> {
                            if(eventName.startsWith("MESSAGE")) {
                                dispatchMessages(self, eventBody, eventName);
                            } else if(eventName.startsWith("GUILD")) {
                                dispatchGuilds(self, eventBody, eventName);
                            }
                        }
                    }
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    throw new DiscordAPIException("Discord Gateway is closed!\nStatus code: " + code + "\nReason: " + reason);
                }

                @Override
                public void onError(Exception ex) {
                    try {
                        throw ex;
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            socket.connect();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close all active clients Gateway connection and shutdown keep alive thread**/
    public static void shutdown() {
        for(Map.Entry<String, DiscordClient> entry: clients.entrySet()) {
            entry.getValue().close();
        }

        keepAliveLatch.countDown();
        keepAliveThread.interrupt();
    }

    /**
     * Close Discord Gateway connection and make bot offline**/
    public void close() {
        socket.close(1001);
    }

    public SelfUser getSelfUSer() {
        return selfUser;
    }

    public Optional<Guild> getGuildById(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + id)
                .get()
                .build();

        try(Response resp = httpClient.newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            JsonNode o = Constants.MAPPER.readTree(res);

            return Optional.of(Guild.fromJson(this, o));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<BaseChannel> getChannelById(String id) {
        HttpUrl.Builder url = Objects.requireNonNull(HttpUrl.parse(Constants.BASE_URL + "/channels/" + id)).newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        try(Response resp = httpClient.newCall(request).execute()) {
            String res = Objects.requireNonNull(resp.body()).string();
            ErrHandler.handle(res);

            return Optional.of(ChannelUtils.switchTypes(this, Constants.MAPPER.readTree(res)));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<Role> getRoleById(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/users/" + id)
                .get()
                .build();

        try(Response resp = httpClient.newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return null; //TODO: Rework
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public Optional<User> getUserById(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/users/" + id)
                .get()
                .build();

        try(Response resp = httpClient.newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(Constants.GSON.fromJson(res, User.class));
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    private void reconnect() {
        socket.reconnect();
        JsonObject o = new JsonObject();
        o.addProperty("op", 6);
        JsonObject d = new JsonObject();
        d.addProperty("token", token);
        d.addProperty("session_id", sessionId);
        d.addProperty("seq", lastSeq);
        o.add("d", d);

        System.out.println("[D.Java]: Trying to reconnect...");

        if(debug) {
            System.out.println("[D.Java]: Sending Resume Packet:\n" + o.toString());
        }

        socket.send(o.toString());
    }
}
