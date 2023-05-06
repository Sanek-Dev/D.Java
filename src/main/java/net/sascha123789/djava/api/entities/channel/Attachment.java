/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;

public class Attachment implements Identifiable {
    private String id;
    private String filename;

    private Attachment(String id, String filename) {
        this.id = id;
        this.filename = filename;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }

    public static Attachment create(String id) {
        return new Attachment(id, "");
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", id);

        return o;
    }

    public static Attachment fromJson(JsonNode json) {
        String id = json.get("id").asText();
        String filename = json.get("filename").asText();

        return new Attachment(id, filename);
    }

    public String getFilename() {
        return filename;
    }
}
