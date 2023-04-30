/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.api.entities.role.Role;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.Request;
import okhttp3.Response;

import java.util.HashSet;
import java.util.Set;

public class Emoji implements Identifiable {
    private String id;
    private String name;
    private Set<Role> roles;
    private User user;
    private boolean managed;
    private boolean animated;
    private boolean available;
    private boolean unicode;

    public static Emoji fromString(String emoji) {
        return new Emoji("", emoji, new HashSet<>(), null, false, false, false, true);
    }

    public static Emoji fromId(DiscordClient client, String guildId, String emojiId) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/guilds/" + guildId + "/emojis/" + emojiId)
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);
            return Emoji.fromJson(client, Constants.GSON.fromJson(res, JsonObject.class));
        } catch(Exception e) {
            e.printStackTrace();
            throw new NullPointerException("Emoji not found!");
        }
    }

    private Emoji(String id, String name, Set<Role> roles, User user, boolean managed, boolean animated, boolean available, boolean unicode) {
        this.id = id;
        this.name = name;
        this.roles = roles;
        this.user = user;
        this.managed = managed;
        this.animated = animated;
        this.available = available;
        this.unicode = unicode;
    }

    public boolean isUnicode() {
        return unicode;
    }

    @Override
    public String toString() {
        return "<" + (animated ? "a" : "") + ":" + name + ":" + id + ">";
    }

    public String toUrlString() {
        return name + ":" + id;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();

        if(!id.isEmpty()) {
            o.addProperty("id", id);
        }

        if(!name.isEmpty()) {
            o.addProperty("name", name);
        }

        o.addProperty("animated", animated);
        return o;
    }

    public static Emoji fromJson(DiscordClient client, JsonObject json) {
        String id = "";
        if(json.get("id") != null) {
            if(!json.get("id").isJsonNull()) {
                id = json.get("id").getAsString();
            }
        }

        String name = "";
        if(json.get("name") != null) {
            if(!json.get("name").isJsonNull()) {
                name = json.get("name").getAsString();
            }
        }

        Set<Role> roles = new HashSet<>();

        if(json.get("roles") != null) {
            if(!json.get("roles").isJsonNull()) {
                JsonArray arr = json.get("roles").getAsJsonArray();

                for(JsonElement el: arr) {
                    JsonObject o = el.getAsJsonObject();

                    roles.add(Role.fromJson(client, o));
                }
            }
        }

        User user = null;
        if(json.get("user") != null) {
            if(!json.get("user").isJsonNull()) {
                user = Constants.GSON.fromJson(json.get("user"), User.class);
            }
        }

         boolean managed = false;
        if(json.get("managed") != null) {
            if(!json.get("managed").isJsonNull()) {
                managed = json.get("managed").getAsBoolean();
            }
        }

        boolean animated = false;
        if(json.get("animated") != null) {
            if(!json.get("animated").isJsonNull()) {
                animated = json.get("animated").getAsBoolean();
            }
        }

        boolean available = false;
        if(json.get("available") != null) {
            if(!json.get("available").isJsonNull()) {
                available = json.get("available").getAsBoolean();
            }
        }

        return new Emoji(id, name, roles, user, managed, animated, available, false);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }

    public String getName() {
        return name;
    }

    /**
     * @return Roles allowed to use this emoji**/
    public Set<Role> getRoles() {
        return roles;
    }

    /**
     * @return User that created this emoji**/
    public User getUser() {
        return user;
    }

    public boolean isManaged() {
        return managed;
    }

    public boolean isAnimated() {
        return animated;
    }

    public boolean isAvailable() {
        return available;
    }
}
