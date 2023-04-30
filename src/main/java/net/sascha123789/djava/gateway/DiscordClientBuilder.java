/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway;

import net.sascha123789.djava.gateway.intents.DiscordIntent;
import net.sascha123789.djava.gateway.intents.DiscordIntents;
import net.sascha123789.djava.gateway.presence.Activity;
import net.sascha123789.djava.gateway.presence.DiscordStatus;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DiscordClientBuilder {
    private String token;
    private boolean debug;
    private List<DiscordIntent> intents;
    private List<Activity> activities;
    private DiscordStatus status;
    private boolean sharding;
    private int shardCount;
    private boolean useRecommendedShardCount;
    private Set<EventAdapter> adapters;

    public DiscordClientBuilder(String token) {
        this.token = token;
        this.debug = false;
        this.intents = DiscordIntents.getNonPrivilegedIntents();
        this.activities = new ArrayList<>();
        this.status = DiscordStatus.ONLINE;
        this.sharding = false;
        this.shardCount = 0;
        this.useRecommendedShardCount = false;
        this.adapters = new HashSet<>();
    }

    public DiscordClientBuilder addEventAdapter(EventAdapter adapter) {
        this.adapters.add(adapter);
        return this;
    }

    public DiscordClientBuilder setToken(String token) {
        this.token = token;
        return this;
    }

    public DiscordClientBuilder setDebug(boolean debug) {
        this.debug = debug;
        return this;
    }

    public DiscordClientBuilder setIntents(DiscordIntent... intents) {
        this.intents = List.of(intents);
        return this;
    }

    public DiscordClientBuilder setIntents(List<DiscordIntent> intents) {
        this.intents = intents;
        return this;
    }

    public DiscordClientBuilder addIntents(DiscordIntent... intents) {
        this.intents.addAll(List.of(intents));
        return this;
    }

    public DiscordClientBuilder addIntents(List<DiscordIntent> intents) {
        this.intents.addAll(intents);
        return this;
    }

    public DiscordClientBuilder setActivities(Activity... activities) {
        this.activities = List.of(activities);
        return this;
    }

    public DiscordClientBuilder setActivities(List<Activity> activities) {
        this.activities = activities;
        return this;
    }

    public DiscordClientBuilder addActivity(Activity activity) {
        this.activities.add(activity);
        return this;
    }

    public DiscordClientBuilder addActivities(Activity... activities) {
        this.activities.addAll(List.of(activities));
        return this;
    }

    public DiscordClientBuilder addActivities(List<Activity> activities) {
        this.activities.addAll(activities);
        return this;
    }

    public DiscordClientBuilder setStatus(DiscordStatus status) {
        this.status = status;
        return this;
    }

    public DiscordClientBuilder setShardCount(int shardCount) {
        this.sharding = true;
        this.shardCount = shardCount;
        this.useRecommendedShardCount = false;
        return this;
    }

    public DiscordClientBuilder setRecommendedShardCount() {
        this.sharding = true;
        this.useRecommendedShardCount = true;
        this.shardCount = 0;
        return this;
    }

    /**
     * @return Built DiscordClient**/
    public DiscordClient build() {
        return new DiscordClient(token, debug, intents, activities, status, sharding, shardCount, useRecommendedShardCount, adapters);
    }
}
