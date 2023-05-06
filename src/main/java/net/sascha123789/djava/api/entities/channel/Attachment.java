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
    private String url;

    private Attachment(String id, String filename, String url) {
        this.id = id;
        this.filename = filename;
        this.url = url;
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
        return new Attachment(id, "", "");
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", id);

        return o;
    }

    public String getUrl() {
        return url;
    }

    public static Attachment fromJson(JsonNode json) {
        String id = json.get("id").asText();
        String filename = json.get("filename").asText();
        String url = json.get("url").asText();

        return new Attachment(id, filename, url);
    }

    public String getFilename() {
        return filename;
    }
}
