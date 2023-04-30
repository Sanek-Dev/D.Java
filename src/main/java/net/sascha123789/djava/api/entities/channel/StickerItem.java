/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;

public class StickerItem implements Identifiable {
    private String name;
    private String id;

    private StickerItem(String name, String id) {
        this.name = name;
        this.id = id;
    }

    public static StickerItem fromJson(JsonObject json) {
        return new StickerItem(json.get("id").getAsString(), json.get("name").getAsString());
    }

    public String getName() {
        return name;
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
