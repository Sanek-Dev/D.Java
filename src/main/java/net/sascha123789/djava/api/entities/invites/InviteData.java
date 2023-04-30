/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.invites;

import com.google.gson.JsonObject;

public class InviteData {
    private int maxUses;
    private int maxAge;
    private boolean temporary;
    private boolean unique;

    public InviteData() {
        this.maxUses = 0;
        this.maxAge = 0;
        this.temporary = false;
        this.unique = false;
    }

    public InviteData setUnique(boolean unique) {
        this.unique = unique;
        return this;
    }

    public InviteData setTemporary(boolean temporary) {
        this.temporary = temporary;
        return this;
    }

    public InviteData setMaxAge(int maxAge) {
        this.maxAge = maxAge;
        return this;
    }

    public InviteData setMaxUses(int maxUses) {
        this.maxUses = maxUses;
        return this;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("max_age", maxAge);
        o.addProperty("max_uses", maxUses);
        o.addProperty("temporary", temporary);
        o.addProperty("unique", unique);

        return o;
    }
}
