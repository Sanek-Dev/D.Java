/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.api.entities.guild.Member;
import net.sascha123789.djava.gateway.DiscordClient;

import java.sql.Timestamp;

public class ThreadMember {
    private String threadId;
    private String userId;
    private Timestamp joinTimestamp;
    private DiscordClient client;
    private Member member;

    private ThreadMember(DiscordClient client, String threadId, String userId, Timestamp joinTimestamp, Member member) {
        this.threadId = threadId;
        this.userId = userId;
        this.joinTimestamp = joinTimestamp;
        this.client = client;
        this.member = member;
    }

    public Member getGuildMember() {
        return member;
    }

    public static ThreadMember fromJson(DiscordClient client, JsonObject json) {
        String threadId = "";
        if(json.get("id") != null) {
            if(!json.get("id").isJsonNull()) {
                threadId = json.get("id").getAsString();
            }
        }

        String userId = "";
        if(json.get("user_id") != null) {
            if(!json.get("user_id").isJsonNull()) {
                userId = json.get("user_id").getAsString();
            }
        }

        String s = json.get("join_timestamp").getAsString();
        s = s.replace("+00:00", "");
        s = s.replace("T", " ");
        Timestamp joinTimestamp = Timestamp.valueOf(s);

        Member member = null;
        if(json.get("member") != null) {
            if(!json.get("member").isJsonNull()) {
                member = Member.fromJson(client, json.get("member").getAsJsonObject());
            }
        }

        return new ThreadMember(client, threadId, userId, joinTimestamp, member);
    }

    public String getThreadId() {
        return threadId;
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getJoinTimestamp() {
        return joinTimestamp;
    }
}
