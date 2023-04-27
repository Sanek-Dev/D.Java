/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc.entities;

import com.google.gson.JsonObject;

import java.time.OffsetDateTime;

public class PresenceTimestamps {
    private OffsetDateTime start;
    private OffsetDateTime end;

    public PresenceTimestamps() {
        this.start = OffsetDateTime.now();
        this.end = null;
    }

    public OffsetDateTime getStart() {
        return start;
    }

    public OffsetDateTime getEnd() {
        return end;
    }

    public PresenceTimestamps setStart(OffsetDateTime time) {
        this.start = time;
        return this;
    }

    public PresenceTimestamps setEnd(OffsetDateTime end) {
        this.end = end;
        return this;
    }

    public JsonObject toJson() {
        JsonObject o = new JsonObject();
        o.addProperty("start", start.toInstant().getEpochSecond());

        if(end != null) {
            o.addProperty("end", end.toInstant().getEpochSecond());
        }

        return o;
    }
}
