/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.guild;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;

public class UnavailableGuild implements Identifiable {
    private String id;
    private boolean unavailable;

    private UnavailableGuild(String id, boolean unavailable) {
        this.id = id;
        this.unavailable = unavailable;
    }

    public static UnavailableGuild fromJson(JsonNode json) {
        String id = json.get("id").asText();
        boolean unavailable = json.get("unavailable").asBoolean();

        return new UnavailableGuild(id, unavailable);
    }

    public boolean isUnavailable() {
        return unavailable;
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
