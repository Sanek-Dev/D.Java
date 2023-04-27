/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.channel.BaseChannel;
import net.sascha123789.djava.api.channel.TextChannel;
import net.sascha123789.djava.api.channel.VoiceChannel;
import net.sascha123789.djava.api.enums.ImageType;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import net.sascha123789.djava.utils.ImageUtils;

import java.io.File;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

public class DiscordActioner {
    private DiscordClient client;

    public DiscordActioner(DiscordClient client) {
        this.client = client;
    }

    public BaseChannel getChannelById(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/channels/" + id))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject obj = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        int type = obj.get("type").getAsInt();
        BaseChannel channel = null;

        switch (type) {
            case 0:
                channel = Constants.GSON.fromJson(obj, TextChannel.class);
                break;
            case 2:
                channel = Constants.GSON.fromJson(obj, VoiceChannel.class);
                break;
        }

        return channel;
    }

    public void bulkOverwriteGuildSlashCommands(String guildId, SlashCommand... commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(arr.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void bulkOverwriteGuildSlashCommands(List<SlashCommand> commands, String guildId) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(arr.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void updateGuildSlashCommand(String name, String guildId) {
        SlashCommand c = null;

        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            if(cmd.getName().equals(name)) {
                c = cmd;
                break;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(Constants.GSON.toJson(c, SlashCommand.class)))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + c.getId()))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public List<SlashCommand> getGuildSlashCommands(String guildId) {
        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);
        List<SlashCommand> list = new ArrayList<>();

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            list.add(cmd);
        }

        return list;
    }

    public void deleteGuildSlashCommandByName(String name, String guildId) {
        String id = "";

        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            if(cmd.getName().equals(name)) {
                id = cmd.getId();
                break;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + id))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void deleteGuildSlashCommand(String cmdId, String guildId) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + cmdId))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void registerGuildSlashCommand(SlashCommand cmd, String guildId) {
        String json = Constants.GSON.toJson(cmd, SlashCommand.class);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle)
                .join();
    }

    public void bulkOverwriteGlobalSlashCommands(SlashCommand... commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(arr.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void bulkOverwriteGlobalSlashCommands(List<SlashCommand> commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(arr.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void updateGlobalSlashCommand(String name) {
        SlashCommand c = null;

        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            if(cmd.getName().equals(name)) {
                c = cmd;
                break;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(Constants.GSON.toJson(c, SlashCommand.class)))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + c.getId()))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public List<SlashCommand> getGlobalSlashCommands() {
        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);
        List<SlashCommand> list = new ArrayList<>();

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            list.add(cmd);
        }

        return list;
    }

    public void deleteGlobalSlashCommandByName(String name) {
        String id = "";

        HttpRequest get = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands?with_localizations=true"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(get, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            if(cmd.getName().equals(name)) {
                id = cmd.getId();
                break;
            }
        }

        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + id))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void deleteGlobalSlashCommand(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + id))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle).join();
    }

    public void registerGlobalSlashCommand(SlashCommand command) {
        String json = Constants.GSON.toJson(command, SlashCommand.class);

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(json))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle)
                .join();
    }

    public void openSelfDmWith(String userId) {
        JsonObject obj = new JsonObject();
        obj.addProperty("recipient_id", userId);

        HttpRequest request = HttpRequest.newBuilder()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(obj.toString()))
                .uri(URI.create(Constants.BASE_URL + "/users/@me/channels"))
                .build();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(ErrHandler::handle)
                .join();
    }

    /**
     * @param file Image file
     * @return Self user on success**/
    public User modifySelfAvatar(File file, ImageType type) {
        JsonObject object = new JsonObject();
        object.addProperty("avatar", ImageUtils.toDataString(file, type));

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(object.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/users/@me"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        return Constants.GSON.fromJson(res.toString(), User.class);
    }

    /**
     * @param url Image url
     * @return Self user on success**/
    public User modifySelfAvatar(String url, ImageType type) {
        JsonObject object = new JsonObject();
        object.addProperty("avatar", ImageUtils.toDataString(url, type));

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(object.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/users/@me"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        return Constants.GSON.fromJson(res.toString(), User.class);
    }

    /**
     * @param username User's username, if changed may cause the user's discriminator to be randomized.
     * @return Self user on success**/
    public User modifySelfUsername(String username) {
        JsonObject object = new JsonObject();
        object.addProperty("username", username);

        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(object.toString()))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .header("Content-Type", "application/json")
                .uri(URI.create(Constants.BASE_URL + "/users/@me"))
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        return Constants.GSON.fromJson(res.toString(), User.class);
    }

    /**
     * @return A user object for a given user ID.**/
    public User getUserFromId(String id) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(Constants.BASE_URL + "/users/" + id))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + client.getToken())
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        return Constants.GSON.fromJson(res.toString(), User.class);
    }

    /**
     * @return The user object of the requester account**/
    public User getSelfUser() {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .uri(URI.create(Constants.BASE_URL + "/users/@me"))
                .header("Authorization", "Bot " + client.getToken())
                .header("User-Agent", Constants.USER_AGENT)
                .build();
        StringBuilder res = new StringBuilder();

        client.getHttpClient().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        return Constants.GSON.fromJson(res.toString(), User.class);
    }
}
