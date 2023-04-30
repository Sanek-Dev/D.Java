/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.invites;

import com.google.gson.JsonObject;

import java.sql.Time;
import java.sql.Timestamp;

public class InviteMetadata {
    private int uses;
    private int maxUses;
    private int maxAge;
    private boolean temporary;
    private Timestamp createdAt;

    private InviteMetadata(int uses, int maxUses, int maxAge, boolean temporary, Timestamp createdAt) {
        this.uses = uses;
        this.maxUses = maxUses;
        this.maxAge = maxAge;
        this.temporary = temporary;
        this.createdAt = createdAt;
    }

    public static InviteMetadata fromJson(JsonObject json) {
        int uses = 0;
        if(json.get("uses") != null) {
            if(!json.get("uses").isJsonNull()) {
                uses = json.get("uses").getAsInt();
            }
        }

        int maxUses = json.get("max_uses").getAsInt();
        int maxAge = json.get("max_age").getAsInt();
        boolean temp = json.get("temporary").getAsBoolean();
        String s = json.get("created_at").getAsString();
        s = s.replace("+00:00", "");
        s = s.replace("T", " ");
        Timestamp createdAt = Timestamp.valueOf(s);

        return new InviteMetadata(uses, maxUses, maxAge, temp, createdAt);
    }

    /**
     * @return Number of times this invite has been used**/
    public int getUses() {
        return uses;
    }

    /**
     * @return Max number of times this invite can be used**/
    public int getMaxUses() {
        return maxUses;
    }

    /**
     * @return Duration (in seconds) after which the invite expires**/
    public int getMaxAge() {
        return maxAge;
    }

    /**
     * @return Whether this invite only grants temporary membership**/
    public boolean isTemporary() {
        return temporary;
    }

    /**
     * @return When this invite was created**/
    public Timestamp getCreatedAt() {
        return createdAt;
    }
}
