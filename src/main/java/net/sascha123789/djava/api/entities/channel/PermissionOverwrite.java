/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.Identifiable;
import net.sascha123789.djava.api.enums.DiscordPermission;

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

    public static PermissionOverwrite createForUser(String id, Set<DiscordPermission> allow, Set<DiscordPermission> deny) {
        return new PermissionOverwrite(id, Type.MEMBER, allow, deny);
    }

    public static PermissionOverwrite createForRole(String id, Set<DiscordPermission> allow, Set<DiscordPermission> deny) {
        return new PermissionOverwrite(id, Type.ROLE, allow, deny);
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("id", id);
        o.addProperty("type", (type == Type.ROLE ? 0 : 1));
        long allow = 0;
        for(DiscordPermission perm: this.allow) {
            allow += perm.getCode();
        }
        long deny = 0;
        for(DiscordPermission perm: this.deny) {
            deny += perm.getCode();
        }

        o.addProperty("allow", String.valueOf(allow));
        o.addProperty("deny", String.valueOf(deny));

        return o;
    }

    public static PermissionOverwrite fromJson(JsonObject json) {
        String id = json.get("id").getAsString();
        int t = json.get("type").getAsInt();
        Type type = (t == 0 ? Type.ROLE : Type.MEMBER);
        long allowBit = Long.parseLong(json.get("allow").getAsString());
        long denyBit = Long.parseLong(json.get("deny").getAsString());

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

    public Set<DiscordPermission> getDenyPermissions() {
        return deny;
    }

    public Set<DiscordPermission> getAllowPermissions() {
        return allow;
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
