/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.lowLevel;

import com.google.gson.JsonObject;

public class GatewayPacket {
    private int op;
    private JsonObject data;
    private String name;

    public GatewayPacket(int op, JsonObject data, String name) {
        this.op = op;
        this.data = data;
        this.name = name;
    }

    public int getOp() {
        return op;
    }

    public JsonObject getData() {
        return data;
    }

    public String getName() {
        return name;
    }
}
