/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.events;

import net.sascha123789.djava.api.SelfUser;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.gateway.DiscordClient;

public class ReadyEvent extends BaseEvent {
    private int apiVersion;
    private String sessionId;
    private String resumeUrl;
    private String appId;
    private SelfUser self;

    public ReadyEvent(DiscordClient client, int apiVersion, String sessionId, String resumeUrl, String appId, SelfUser self) {
        super(client);
        this.apiVersion = apiVersion;
        this.sessionId = sessionId;
        this.resumeUrl = resumeUrl;
        this.appId = appId;
        this.self = self;
    }

    public SelfUser getSelfUser() {
        return self;
    }

    public int getApiVersion() {
        return apiVersion;
    }

    public String getSessionId() {
        return sessionId;
    }

    public String getResumeUrl() {
        return resumeUrl;
    }

    public String getApplicationId() {
        return appId;
    }
}
