/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.common.collect.ImmutableSet;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.enums.DiscordPermission;
import net.sascha123789.djava.utils.Constants;

import java.util.HashSet;
import java.util.Set;

public class PermissionOverwrite implements Identifiable {
    private String id;
    private Type type;
    private Set<DiscordPermission> allow;
    private Set<DiscordPermission> deny;

    private PermissionOverwrite(String id, Type type, Set<DiscordPermission> allow, Set<DiscordPermission> deny) {
        this.allow = allow;
        this.deny = deny;
        this.id = id;
        this.type = type;
    }

    public static final PermissionOverwrite createForUser(String id, Set<DiscordPermission> allow, Set<DiscordPermission> deny) {
        return new PermissionOverwrite(id, Type.MEMBER, allow, deny);
    }

    public static final PermissionOverwrite createForRole(String id, Set<DiscordPermission> allow, Set<DiscordPermission> deny) {
        return new PermissionOverwrite(id, Type.ROLE, allow, deny);
    }

    public final JsonNode toJson() {
        ObjectNode o = Constants.MAPPER.createObjectNode();
        o.put("id", id);
        o.put("type", (type == Type.ROLE ? 0 : 1));
        long allow = 0;
        for(DiscordPermission perm: this.allow) {
            allow += perm.getCode();
        }
        long deny = 0;
        for(DiscordPermission perm: this.deny) {
            deny += perm.getCode();
        }

        o.put("allow", String.valueOf(allow));
        o.put("deny", String.valueOf(deny));

        return o;
    }

    public static PermissionOverwrite fromJson(JsonNode json) {
        String id = json.get("id").asText();
        int t = json.get("type").asInt();
        Type type = (t == 0 ? Type.ROLE : Type.MEMBER);
        long allowBit = Long.parseLong(json.get("allow").asText());
        long denyBit = Long.parseLong(json.get("deny").asText());

        Set<DiscordPermission> allow = new HashSet<>();
        Set<DiscordPermission> deny = new HashSet<>();

        for(DiscordPermission el: DiscordPermission.values()) {
            if((el.getCode() & allowBit) == el.getCode()) {
                allow.add(el);
            }
        }

        for(DiscordPermission el: DiscordPermission.values()) {
            if((el.getCode() & denyBit) == el.getCode()) {
                deny.add(el);
            }
        }

        return new PermissionOverwrite(id, type, allow, deny);
    }

    public ImmutableSet<DiscordPermission> getDenyPermissions() {
        return ImmutableSet.copyOf(deny);
    }

    public ImmutableSet<DiscordPermission> getAllowPermissions() {
        return ImmutableSet.copyOf(allow);
    }

    /**
     * @return Role or user id**/
    @Override
    public String getId() {
        return id;
    }

    /**
     * @return Role or user id**/
    @Override
    public Long getIdAsLong() {
        return Long.parseLong(id);
    }

    public static enum Type {
        ROLE, MEMBER;
    }
}
