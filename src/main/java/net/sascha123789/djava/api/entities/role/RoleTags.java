/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.role;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

public class RoleTags {
    private String botId;
    private String integrationId;
    private boolean boosterRole;
    private boolean purchaseRole;
    private boolean linkedRole;

    private RoleTags(String botId, String integrationId, boolean boosterRole, boolean purchaseRole, boolean linkedRole) {
        this.botId = botId;
        this.integrationId = integrationId;
        this.boosterRole = boosterRole;
        this.purchaseRole = purchaseRole;
        this.linkedRole = linkedRole;
    }

    /**
     * @return The id of the bot this role belongs to**/
    public String getBotId() {
        return botId;
    }

    /**
     * @return The id of the integration this role belongs to**/
    public String getIntegrationId() {
        return integrationId;
    }

    public boolean isBoosterRole() {
        return boosterRole;
    }

    public boolean isPurchaseRole() {
        return purchaseRole;
    }

    public boolean isLinkedRole() {
        return linkedRole;
    }

    public static RoleTags fromJson(JsonNode json) {
        String botId = "";
        if(json.get("bot_id") != null) {
            if(!json.get("bot_id").isNull()) {
                botId = json.get("bot_id").asText();
            }
        }

        String integrationId = "";
        if(json.get("integration_id") != null) {
            if(!json.get("integration_id").isNull()) {
                integrationId = json.get("integration_id").asText();
            }
        }

        boolean boosterRole = json.get("premium_subscriber") != null;
        boolean purchaseRole = json.get("available_for_purchase") != null;
        boolean linkedRole = json.get("guild_connections") != null;

        return new RoleTags(botId, integrationId, boosterRole, purchaseRole, linkedRole);
    }
}
