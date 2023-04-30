package net.sascha123789.djava.gateway;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.channel.BaseChannel;
import net.sascha123789.djava.api.entities.channel.Emoji;
import net.sascha123789.djava.api.entities.channel.Message;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.api.enums.DiscordLanguage;
import net.sascha123789.djava.api.interactions.slash.SlashCommandUseEvent;
import net.sascha123789.djava.gateway.events.*;
import net.sascha123789.djava.gateway.intents.DiscordIntent;
import net.sascha123789.djava.gateway.presence.Activity;
import net.sascha123789.djava.gateway.presence.ActivityType;
import net.sascha123789.djava.gateway.presence.DiscordStatus;
import net.sascha123789.djava.utils.ChannelUtils;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.DiscordAPIException;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.*;
import org.apache.commons.lang3.SystemUtils;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.jetbrains.annotations.NotNull;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLParameters;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DiscordClient {
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

    /**
     * @param token Bot token
     * @param intents Bot intents**/
    public DiscordClient(String token, List<DiscordIntent> intents, Set<EventAdapter> adapters) {
        this.token = token;

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

        JsonObject gatewayRes = Constants.GSON.fromJson(getGatewayRes.toString(), JsonObject.class);

        this.gatewayUrl = gatewayRes.get("url").getAsString() + "?v=10&encoding=json";
        this.recommendedShards = gatewayRes.get("shards").getAsInt();
        this.intents = intents;
    }

    /**
     * @param token Bot token
     * @param debugLogging Debug logging enabled
     * @param intents Bot intents**/
    public DiscordClient(String token, boolean debugLogging, List<DiscordIntent> intents, Set<EventAdapter> adapters) {
        this.token = token;

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

        JsonObject gatewayRes = Constants.GSON.fromJson(getGatewayRes.toString(), JsonObject.class);

        this.gatewayUrl = gatewayRes.get("url").getAsString() + "?v=10&encoding=json";
        this.recommendedShards = gatewayRes.get("shards").getAsInt();
        this.intents = intents;
    }

    /**
     * @param token Bot token
     * @param debugLogging Debug logging enabled
     * @param intents Bot intents
     * @param status Bot status
     * @param activities Bot activities**/
    public DiscordClient(String token, boolean debugLogging, List<DiscordIntent> intents, List<Activity> activities, DiscordStatus status, boolean sharding, int shardCount, boolean useRecommendedShardCount, Set<EventAdapter> adapters) {
        this.token = token;

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

        JsonObject gatewayRes = Constants.GSON.fromJson(getGatewayRes.toString(), JsonObject.class);

        this.gatewayUrl = gatewayRes.get("url").getAsString() + "?v=10&encoding=json";
        this.recommendedShards = gatewayRes.get("shards").getAsInt();
        this.intents = intents;
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

                    JsonObject eventObj = Constants.GSON.fromJson(message, JsonObject.class);
                    JsonObject eventBody = null;

                    if(!eventObj.get("d").isJsonNull()) {
                        eventBody = eventObj.get("d").getAsJsonObject();
                    }

                    int eventOp = 0;
                    String eventName = "";

                    if(!eventObj.get("op").isJsonNull()) {
                        eventOp = eventObj.get("op").getAsInt();
                    }

                    if(!eventObj.get("t").isJsonNull()) {
                        eventName = eventObj.get("t").getAsString();
                    }

                    if(!eventObj.get("s").isJsonNull()) {
                        lastSeq = eventObj.get("s").getAsInt();
                    }

                    switch (eventOp) {
                        case 10:
                            heartbeatInterval = eventBody.get("heartbeat_interval").getAsInt();
                            JsonObject heartbeatObject = new JsonObject();
                            heartbeatObject.addProperty("op", 1);
                            heartbeatObject.add("d", JsonNull.INSTANCE);

                            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
                            scheduler.scheduleAtFixedRate(() -> {
                                socket.send(heartbeatObject.toString());

                                for(EventAdapter adapter: adapters) {
                                    adapter.onHeartbeat(new HeartbeatEvent(self, heartbeatInterval));
                                }
                            }, 1000, (heartbeatInterval - 2000), TimeUnit.MILLISECONDS);

                            for(EventAdapter adapter: adapters) {
                                adapter.onHello(new HelloEvent(self, heartbeatInterval));
                            }

                            /* Identify */

                            if(debug) {
                                System.out.println("[D.Java]: Identifying gateway client...");
                            }

                            JsonObject identify = new JsonObject();
                            identify.addProperty("token", token);

                            JsonObject identifyProperties = new JsonObject();
                            identifyProperties.addProperty("os", SystemUtils.OS_NAME);
                            identifyProperties.addProperty("browser", "D.Java");
                            identifyProperties.addProperty("device", "D.Java");

                            identify.add("properties", identifyProperties);

                            int[] shards;

                            if(sharding) {
                                if(useRecommendedShardCount) {
                                    shards = new int[]{0, recommendedShards};
                                } else {
                                    shards = new int[]{0, shardCount};
                                }

                                JsonArray arr = new JsonArray();
                                arr.add(shards[0]);
                                arr.add(shards[1]);

                                identify.add("shard", arr);
                            }

                            JsonObject presence = new JsonObject();
                            presence.add("since", JsonNull.INSTANCE);
                            presence.addProperty("status", (status == DiscordStatus.DO_NOT_DISTURB ? "dnd" : (status == DiscordStatus.OFFLINE ? "offline" : (status == DiscordStatus.IDLE ? "idle" : "online"))));
                            presence.addProperty("afk", false);

                            if(!activities.isEmpty()) {
                                JsonArray arr = new JsonArray();

                                for(Activity activity: activities) {
                                    JsonObject o = new JsonObject();
                                    o.addProperty("name", activity.getName());
                                    o.addProperty("type", (activity.getType() == ActivityType.COMPETING ? 5 : (activity.getType() == ActivityType.LISTENING ? 2 : (activity.getType() == ActivityType.PLAYING ? 0 : (activity.getType() == ActivityType.STREAMING ? 1 : 3)))));

                                    if(!activity.getUrl().isEmpty()) {
                                        o.addProperty("url", activity.getUrl());
                                    }

                                    if(!activity.getDetails().isEmpty()) {
                                        o.addProperty("details", activity.getDetails());
                                    }

                                    if(!activity.getState().isEmpty()) {
                                        o.addProperty("state", activity.getState());
                                    }

                                    arr.add(o);
                                }

                                presence.add("activities", arr);
                            } else {
                                presence.add("activities", new JsonArray());
                            }

                            identify.add("presence", presence);
                            long code = 0L;

                            for(DiscordIntent intent: intents) {
                                code += intent.getCode();
                            }

                            identify.addProperty("intents", code);

                            JsonObject identifyEvent = new JsonObject();
                            identifyEvent.add("d", identify);
                            identifyEvent.addProperty("op", 2);

                            if(debug) {
                                System.out.println("[D.Java]: Sending Identify packet:\n" + identifyEvent.toString());
                            }

                            socket.send(identifyEvent.toString());

                            if(debug) {
                                System.out.println("[D.Java]: Successfully identified gateway client!");
                            }

                            break;
                        case 7:
                            self.reconnect();
                            break;
                    }

                    switch (eventName) {
                        case "READY":
                            int v = eventBody.get("v").getAsInt();
                            String sessionId = eventBody.get("session_id").getAsString();
                            String resumeUrl = eventBody.get("resume_gateway_url").getAsString();
                            String appId = eventBody.get("application").getAsJsonObject().get("id").getAsString();
                            self.apiVersion = v;
                            self.sessionId = sessionId;
                            self.resumeUrl = resumeUrl;
                            self.appId = appId;
                            self.running = true;
                            self.selfUser = SelfUser.fromJson(self, eventBody.get("user").getAsJsonObject());

                            for(EventAdapter adapter: adapters) {
                                adapter.onReady(new ReadyEvent(self, v, sessionId, resumeUrl, appId, selfUser));
                            }

                            break;

                        case "INTERACTION_CREATE":
                            int type = eventBody.get("type").getAsInt();
                            String id = eventBody.get("id").getAsString();
                            String app = eventBody.get("application_id").getAsString();
                            String token = eventBody.get("token").getAsString();
                            String locale = eventBody.get("locale").getAsString();
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
                                    if(!eventBody.get("channel_id").isJsonNull()) {
                                        channelId = eventBody.get("channel_id").getAsString();
                                    }
                                }

                                String guildId = "";

                                if(eventBody.get("guild_id") != null) {
                                    if(!eventBody.get("guild_id").isJsonNull()) {
                                        guildId = eventBody.get("guild_id").getAsString();;
                                    }
                                }

                                String guildLocale = "";

                                if(eventBody.get("guild_locale") != null) {
                                    if(!eventBody.get("guild_locale").isJsonNull()) {
                                        guildLocale = eventBody.get("guild_locale").getAsString();
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
                                    if(!eventBody.get("channel").isJsonNull()) {
                                        channel = ChannelUtils.switchTypes(self, eventBody.get("channel").getAsJsonObject());
                                    }
                                }

                                for(EventAdapter adapter: adapters) {
                                    adapter.onSlashCommandUse(new SlashCommandUseEvent(channel, self, id, token, app, lang, channelId, guildId, l));
                                }
                            }

                            break;
                        case "MESSAGE_CREATE":
                            String guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageCreate(new MessageCreateEvent(self, Message.fromJson(self, eventBody), guildId));
                            }

                            break;
                        case "MESSAGE_UPDATE":
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageUpdate(new MessageUpdateEvent(self, Message.fromJson(self, eventBody), guildId));
                            }

                            break;
                        case "MESSAGE_DELETE":
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageDelete(new MessageDeleteEvent(self, eventBody.get("id").getAsString(), eventBody.get("channel_id").getAsString(), guildId));
                            }

                            break;
                        case "MESSAGE_DELETE_BULK":
                        {
                            Set<String> ids = new HashSet<>();
                            JsonArray arr = eventBody.get("ids").getAsJsonArray();

                            for(JsonElement el: arr) {
                                ids.add(el.getAsString());
                            }

                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageBulkDelete(new MessageDeleteBulkEvent(self, ids, eventBody.get("channel_id").getAsString(), guildId));
                            }
                        }
                            break;
                        case "MESSAGE_REACTION_ADD":
                        {
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageReactionAdd(new MessageReactionAddEvent(self, eventBody.get("user_id").getAsString(), eventBody.get("channel_id").getAsString(), eventBody.get("message_id").getAsString(), guildId, Emoji.fromJson(self, eventBody.get("emoji").getAsJsonObject())));
                            }
                        }
                            break;
                        case "MESSAGE_REACTION_REMOVE":
                        {
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageReactionRemove(new MessageReactionRemoveEvent(self, eventBody.get("user_id").getAsString(), eventBody.get("channel_id").getAsString(), eventBody.get("message_id").getAsString(), guildId, Emoji.fromJson(self, eventBody.get("emoji").getAsJsonObject())));
                            }
                        }
                            break;
                        case "MESSAGE_REACTION_REMOVE_ALL":
                        {
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageReactionRemoveAll(new MessageReactionRemoveAllEvent(self, eventBody.get("channel_id").getAsString(), eventBody.get("message_id").getAsString(), guildId));
                            }
                        }
                            break;
                        case "MESSAGE_REACTION_REMOVE_EMOJI":
                        {
                            guildId = "";
                            if(eventBody.get("guild_id") != null) {
                                if(!eventBody.get("guild_id").isJsonNull()) {
                                    guildId = eventBody.get("guild_id").getAsString();
                                }
                            }

                            for(EventAdapter adapter: adapters) {
                                adapter.onMessageReactionRemoveAllEmoji(new MessageReactionRemoveAllEmojiEvent(self, eventBody.get("channel_id").getAsString(), guildId, eventBody.get("message_id").getAsString(), Emoji.fromJson(self, eventBody.get("emoji").getAsJsonObject())));
                            }
                        }
                            break;
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

            new Thread(() -> {
                CountDownLatch latch = new CountDownLatch(1);

                try {
                    latch.await();
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }).start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Close Discord Gateway connection and make bot offline**/
    public void close() {
        socket.close(1001);
    }

    public SelfUser getSelfUSer() {
        return selfUser;
    }

    public Optional<BaseChannel> getChannelById(String id) {
        HttpUrl.Builder url = HttpUrl.parse(Constants.BASE_URL + "/channels/" + id).newBuilder()
                .addQueryParameter("with_member", "true");

        Request request = new Request.Builder()
                .url(url.build().toString())
                .get()
                .build();

        try(Response resp = httpClient.newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            System.out.println(res);

            return Optional.of(ChannelUtils.switchTypes(this, Constants.GSON.fromJson(res, JsonObject.class)));
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
