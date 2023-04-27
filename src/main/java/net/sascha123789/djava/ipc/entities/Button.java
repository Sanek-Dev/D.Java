/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonObject;

public class Button {
    private String label;
    private String url;

    public Button(String label, String url) {
        this.label = label;
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public String getLabel() {
        return label;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("label", label);
        o.addProperty("url", url);
        return o;
    }
}
