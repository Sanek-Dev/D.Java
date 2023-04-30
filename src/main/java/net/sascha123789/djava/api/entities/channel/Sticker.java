/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.gateway.DiscordClient;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;
import okhttp3.Request;
import okhttp3.Response;

import java.util.Optional;

public class Sticker implements Identifiable {
    private String id;
    private String packId;
    private String name;
    private String description;
    private StickerType type;
    private boolean available;
    private String guildId;
    private User creator;
    private int sortValue;

    private Sticker(String id, String packId, String name, String description, StickerType type, boolean available, String guildId, User creator, int sortValue) {
        this.id = id;
        this.packId = packId;
        this.description = description;
        this.type = type;
        this.available = available;
        this.guildId = guildId;
        this.creator = creator;
        this.sortValue = sortValue;
    }

    public static Optional<Sticker> fromId(DiscordClient client, String id) {
        Request request = new Request.Builder()
                .url(Constants.BASE_URL + "/stickers/" + id)
                .get()
                .build();

        try(Response resp = client.getHttpClient().newCall(request).execute()) {
            String res = resp.body().string();
            ErrHandler.handle(res);

            return Optional.of(Sticker.fromJson(Constants.GSON.fromJson(res, JsonObject.class)));
        } catch(Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static Sticker fromJson(JsonObject json) {
        String id = json.get("id").getAsString();
        String packId = "";
        if(json.get("pack_id") != null) {
            if(!json.get("pack_id").isJsonNull()) {
                packId = json.get("pack_id").getAsString();
            }
        }

        String name = json.get("name").getAsString();
        String description = "";

        if(json.get("description") != null) {
            if(!json.get("description").isJsonNull()) {
                description = json.get("description").getAsString();
            }
        }

        int t = json.get("type").getAsInt();
        StickerType type = (t == 1 ? StickerType.STANDARD : StickerType.GUILD);

        boolean available = false;
        if(json.get("available") != null) {
            if(!json.get("available").isJsonNull()) {
                available = json.get("available").getAsBoolean();
            }
        }

        String guildId = "";
        if(json.get("guild_id") != null) {
            if(!json.get("guild_id").isJsonNull()) {
                guildId = json.get("guild_id").getAsString();
            }
        }

        User user = null;
        if(json.get("user") != null) {
            if(!json.get("user").isJsonNull()) {
                user = Constants.GSON.fromJson(json.get("user").getAsJsonObject(), User.class);
            }
        }

        int sortValue = 0;
        if(json.get("sort_value") != null) {
            if(!json.get("sort_value").isJsonNull()) {
                sortValue = json.get("sort_value").getAsInt();
            }
        }

        return new Sticker(id, packId, name, description, type, available, guildId, user, sortValue);
    }

    public String getPackId() {
        return packId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public StickerType getType() {
        return type;
    }

    public boolean isAvailable() {
        return available;
    }

    public String getGuildId() {
        return guildId;
    }

    public User getCreator() {
        return creator;
    }

    public int getSortValue() {
        return sortValue;
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
