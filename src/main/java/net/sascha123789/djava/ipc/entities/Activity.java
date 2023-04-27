/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Activity {
    private String details;
    private String state;
    private boolean instance;
    private PresenceTimestamps timestamps;
    private PresenceParty party;
    private PresenceAssets assets;
    private PresenceSecrets secrets;
    private Button btn1;
    private Button btn2;

    public Activity(boolean instance) {
        this.instance = instance;
        this.details = null;
        this.state = null;
        this.timestamps = null;
        this.party = null;
        this.assets = null;
        this.secrets = null;
        this.btn1 = null;
        this.btn2 = null;
    }

    public Activity setButton2(Button btn2) {
        this.btn2 = btn2;
        return this;
    }

    public Activity setButton1(Button btn1) {
        this.btn1 = btn1;
        return this;
    }

    public Activity setButtons(Button btn1, Button btn2) {
        this.btn1 = btn1;
        this.btn2 = btn2;
        return this;
    }

    public Activity setSecrets(PresenceSecrets secrets) {
        this.secrets = secrets;
        return this;
    }

    public Activity setAssets(PresenceAssets assets) {
        this.assets = assets;
        return this;
    }

    public Activity setParty(PresenceParty party) {
        this.party = party;
        return this;
    }

    public Activity setTimestamps(PresenceTimestamps timestamps) {
        this.timestamps = timestamps;
        return this;
    }

    public Activity setState(String state) {
        this.state = state;
        return this;
    }

    public Activity setDetails(String details) {
        this.details = details;
        return this;
    }

    public Activity setInstance(boolean instance) {
        this.instance = instance;
        return this;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("instance", instance);

        if(state != null) {
            o.addProperty("state", state);
        }

        if(details != null) {
            o.addProperty("details", details);
        }

        if(timestamps != null) {
            o.add("timestamps", timestamps.toJson());
        }

        if(party != null) {
            o.add("party", party.toJson());
        }

        if(assets != null) {
            o.add("assets", assets.toJson());
        }

        if(secrets != null) {
            o.add("secrets", secrets.toJson());
        }
        JsonArray buttons = new JsonArray();

        if(btn1 != null) {
            buttons.add(btn1.toJson());
        }

        if(btn2 != null) {
            buttons.add(btn2.toJson());
        }

        if(!buttons.isEmpty()) {
            o.add("buttons", buttons);
        }

        return o;
    }
}
