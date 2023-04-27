/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonObject;

public class PresenceSecrets {
    private String join;
    private String spectate;
    private String match;

    public PresenceSecrets(String join, String spectate, String match) {
        this.join = join;
        this.spectate = spectate;
        this.match = match;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("join", join);
        o.addProperty("spectate", spectate);
        o.addProperty("match", match);

        return o;
    }
}
