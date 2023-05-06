/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.channel;

import com.fasterxml.jackson.databind.JsonNode;
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
    private ThreadChannel thread;

    private ThreadMember(ThreadChannel thread, DiscordClient client, String threadId, String userId, Timestamp joinTimestamp, Member member) {
        this.threadId = threadId;
        this.userId = userId;
        this.joinTimestamp = joinTimestamp;
        this.client = client;
        this.member = member;
        this.thread = thread;
    }

    public Member getGuildMember() {
        return member;
    }

    public ThreadChannel getThread() {
        return thread;
    }

    public static ThreadMember fromJson(DiscordClient client, JsonNode json) {
        String threadId = "";
        if(json.get("id") != null) {
            if(!json.get("id").isNull()) {
                threadId = json.get("id").asText();
            }
        }

        String userId = "";
        if(json.get("user_id") != null) {
            if(!json.get("user_id").isNull()) {
                userId = json.get("user_id").asText();
            }
        }

        String s = json.get("join_timestamp").asText();
        s = s.replace("+00:00", "");
        s = s.replace("T", " ");
        Timestamp joinTimestamp = Timestamp.valueOf(s);

        ThreadChannel thread = null;
        if(!threadId.isEmpty()) {
            thread = client.getChannelById(threadId).get().asThreadChannel();
        }

        Member member = null;
        if(json.get("member") != null) {
            if(!json.get("member").isNull()) {
                if(thread != null) {
                    member = Member.fromJson(client, json.get("member"), thread.getGuildId());
                }
            }
        }

        if(client.isOptimized()) {
            System.gc();
        }

        return new ThreadMember(thread, client, threadId, userId, joinTimestamp, member);
    }

    public String getUserId() {
        return userId;
    }

    public Timestamp getJoinTimestamp() {
        return joinTimestamp;
    }
}
