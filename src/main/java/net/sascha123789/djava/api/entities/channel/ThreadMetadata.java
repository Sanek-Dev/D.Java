/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
import com.google.gson.JsonObject;

import java.sql.Timestamp;

public class ThreadMetadata {
    private boolean archived;
    private int archiveDuration;
    private Timestamp archiveTimestamp;
    private boolean locked;
    private boolean invitable;
    private Timestamp createTimestamp;
    private String guildId;

    private ThreadMetadata(String guildId, boolean archived, int archiveDuration, Timestamp archiveTimestamp, boolean locked, boolean invitable, Timestamp createTimestamp) {
        this.archived = archived;
        this.archiveDuration = archiveDuration;
        this.locked = locked;
        this.archiveTimestamp = archiveTimestamp;
        this.invitable = invitable;
        this.createTimestamp = createTimestamp;
        this.guildId = guildId;
    }

    public static ThreadMetadata fromJson(String guildId, JsonNode json) {
        boolean archived = json.get("archived").asBoolean();
        int archiveDuration = json.get("auto_archive_duration").asInt();
        Timestamp archiveTimestamp = null;
        if(json.get("archive_timestamp") != null) {
            if(!json.get("archive_timestamp").isNull()) {
                String s = json.get("archive_timestamp").asText();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");
                archiveTimestamp = Timestamp.valueOf(s);
            }
        }
        boolean locked = json.get("locked").asBoolean();
        boolean invitable = true;

        if(json.get("invitable") != null) {
            if(!json.get("invitable").isNull()) {
                invitable = json.get("invitable").asBoolean();
            }
        }

        Timestamp createTimestamp = null;
        if(json.get("create_timestamp") != null) {
            if(!json.get("create_timestamp").isNull()) {
                String s = json.get("create_timestamp").asText();
                s = s.replace("+00:00", "");
                s = s.replace("T", " ");

                createTimestamp = Timestamp.valueOf(s);
            }
        }

        return new ThreadMetadata(guildId, archived, archiveDuration, archiveTimestamp, locked, invitable, createTimestamp);
    }

    public boolean isArchived() {
        return archived;
    }

    public int getArchiveDuration() {
        return archiveDuration;
    }

    public Timestamp getArchiveTimestamp() {
        return archiveTimestamp;
    }

    public boolean isLocked() {
        return locked;
    }

    public boolean isInvitable() {
        return invitable;
    }

    public Timestamp getCreateTimestamp() {
        return createTimestamp;
    }
}
