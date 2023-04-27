/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.lowLevel;

import com.google.gson.JsonObject;

public class HttpPacket {
    private String str;
    private JsonObject data;

    public HttpPacket(String str, JsonObject data) {
        this.str = str;
        this.data = data;
    }

    public String getStr() {
        return str;
    }

    public JsonObject getData() {
        return data;
    }
}
