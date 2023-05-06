/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.invites;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;

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

    public static InviteMetadata fromJson(JsonNode json) {
        int uses = 0;
        if(json.get("uses") != null) {
            if(!json.get("uses").isNull()) {
                uses = json.get("uses").asInt();
            }
        }

        int maxUses = json.get("max_uses").asInt();
        int maxAge = json.get("max_age").asInt();
        boolean temp = json.get("temporary").asBoolean();
        String s = json.get("created_at").asText();
        s = StringUtils.replace(s,  "+00:00", "");
        s = StringUtils.replace(s, "T", " ");
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
