/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonObject;

public class PresenceAssets {
    private String largeImg;
    private String largeText;
    private String smallImg;
    private String smallText;

    public PresenceAssets() {
        this.largeImg = null;
        this.largeText = null;
        this.smallText = null;
        this.smallImg = null;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();

        if(largeImg != null) {
            o.addProperty("large_image", largeImg);
        }

        if(largeText != null) {
            o.addProperty("large_text", largeText);
        }

        if(smallText != null) {
            o.addProperty("small_text", smallText);
        }

        if(smallImg != null) {
            o.addProperty("small_image", smallImg);
        }

        return o;
    }

    public PresenceAssets setSmallText(String text) {
        this.smallText = text;
        return this;
    }

    public PresenceAssets setSmallImage(String key) {
        this.smallImg = key;
        return this;
    }

    public PresenceAssets setLargeText(String text) {
        this.largeText = text;
        return this;
    }

    public PresenceAssets setLargeImage(String key) {
        this.largeImg = key;
        return this;
    }
}
