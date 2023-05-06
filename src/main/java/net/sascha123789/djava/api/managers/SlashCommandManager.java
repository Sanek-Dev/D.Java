/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.managers;

import com.google.common.collect.ImmutableList;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.interactions.slash.SlashCommand;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SlashCommandManager implements DiscordManager {
    private DiscordClient client;

    public SlashCommandManager(DiscordClient client) {
        this.client = client;
    }

    public SlashCommand.Builder createBuilder(String name, String description) {
        return new SlashCommand.Builder(client, name, description);
    }



    private static void putUuid(String id, String uuid) {
        try {
            Class<?> cls = DiscordClient.class;
            Field f = cls.getDeclaredField("uuids");
            f.setAccessible(true);

            Map<String, String> map = (Map<String, String>) f.get(cls);

            map.put(id, uuid);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ImmutableList<SlashCommand> getGlobalSlashCommands() {
        Request get = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands?with_localizations=true")
                .get().build();

        StringBuilder res = new StringBuilder();

        try(Response resp = client.getHttpClient().newCall(get).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
            res.append(str);
        } catch(Exception e) {
            e.printStackTrace();
        }

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);
        List<SlashCommand> list = new ArrayList<>();

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            list.add(cmd);
        }

        return ImmutableList.copyOf(list);
    }

    public void registerGlobalSlashCommand(SlashCommand command) {
        String json = Constants.GSON.toJson(command, SlashCommand.class);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands")
                .post(RequestBody.create(json, MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            String id = Constants.GSON.fromJson(res, JsonObject.class).get("id").getAsString();

            putUuid(id, client.getUuid());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGlobalSlashCommand(String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + id)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGlobalSlashCommandByName(String name) {
        String id = "";

        List<SlashCommand> cmds = getGlobalSlashCommands();

        for(SlashCommand cmd: cmds) {
            if(cmd.getName().equals(name)) {
                id = cmd.getId();
                break;
            }
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + id)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGlobalSlashCommand(String name) {
        SlashCommand c = null;

        List<SlashCommand> cmds = getGlobalSlashCommands();

        for(SlashCommand cmd: cmds) {
            if(cmd.getName().equals(name)) {
                c = cmd;
                break;
            }
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands/" + c.getId())
                        .patch(RequestBody.create(Constants.GSON.toJson(c, SlashCommand.class), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();

            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void bulkOverwriteGlobalSlashCommands(List<SlashCommand> commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands")
                .put(RequestBody.create(arr.toString(), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void bulkOverwriteGlobalSlashCommands(SlashCommand... commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/commands")
                .put(RequestBody.create(arr.toString(), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void registerGuildSlashCommand(SlashCommand cmd, String guildId) {
        String json = Constants.GSON.toJson(cmd, SlashCommand.class);

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands")
                .post(RequestBody.create(json, MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = Objects.requireNonNull(resp.body()).string();
            ErrHandler.handle(str);

            String id = Constants.GSON.fromJson(str, JsonObject.class).get("id").getAsString();
            putUuid(id, client.getUuid());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void deleteGuildSlashCommand(String cmdId, String guildId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + cmdId)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public ImmutableList<SlashCommand> getGuildSlashCommands(String guildId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands?with_localizations=true")
                .get().build();

        StringBuilder res = new StringBuilder();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
            res.append(str);
        } catch(Exception e) {
            e.printStackTrace();
        }

        JsonArray arr = Constants.GSON.fromJson(res.toString(), JsonArray.class);
        List<SlashCommand> list = new ArrayList<>();

        for(JsonElement el: arr) {
            JsonObject o = el.getAsJsonObject();
            SlashCommand cmd = Constants.GSON.fromJson(o, SlashCommand.class);

            list.add(cmd);
        }

        return ImmutableList.copyOf(list);
    }

    public void deleteGuildSlashCommandByName(String name, String guildId) {
        String id = "";

        List<SlashCommand> list = getGuildSlashCommands(guildId);

        for(SlashCommand cmd: list) {
            if(cmd.getName().equals(name)) {
                id = cmd.getId();
                break;
            }
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + id)
                .delete().build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void updateGuildSlashCommand(String name, String guildId) {
        SlashCommand c = null;

        List<SlashCommand> list = getGuildSlashCommands(guildId);

        for(SlashCommand cmd: list) {
            if(cmd.getName().equals(name)) {
                c = cmd;
                break;
            }
        }

        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands/" + c.getId())
                        .patch(RequestBody.create(Constants.GSON.toJson(c, SlashCommand.class), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String str = resp.body().string();
            ErrHandler.handle(str);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void bulkOverwriteGuildSlashCommands(List<SlashCommand> commands, String guildId) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        Request request = new Request.Builder()
                .url((Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands"))
                .put(RequestBody.create(arr.toString(), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public void bulkOverwriteGuildSlashCommands(String guildId, SlashCommand... commands) {
        JsonArray arr = new JsonArray();

        for(SlashCommand command: commands) {
            arr.add(Constants.GSON.toJsonTree(command, SlashCommand.class));
        }

        Request request = new Request.Builder()
                .url((Constants.BASE_URL + "/applications/" + client.getApplicationId() + "/guilds/" + guildId + "/commands"))
                .put(RequestBody.create(arr.toString(), MediaType.parse("application/json"))).build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
