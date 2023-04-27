/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class PresenceParty {
    private String id;
    private int[] size;

    public PresenceParty(int current, int max) {
        this.size = new int[]{current, max};
        this.id = null;
    }

    public String getId() {
        return id;
    }

    public int[] getSize() {
        return size;
    }

    public PresenceParty setCurrentSize(int current) {
        this.size[0] = current;
        return this;
    }

    public PresenceParty setMaxSize(int max) {
        this.size[1] = max;
        return this;
    }

    public PresenceParty setId(String id) {
        this.id = id;
        return this;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        JsonArray arr = new JsonArray();
        arr.add(size[0]);
        arr.add(size[1]);
        o.add("size", arr);

        if(id != null) {
            o.addProperty("id", id);
        }

        return o;
    }
}
