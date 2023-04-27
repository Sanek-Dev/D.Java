/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.gateway.presence;

public class Activity {
    private String name;
    private ActivityType type;
    private String url;
    private String details;
    private String state;

    /**
     * @return Activity state**/
    public String getState() {
        return state;
    }

    /**
     * @return Activity details**/
    public String getDetails() {
        return details;
    }

    /**
     * @return Stream url**/
    public String getUrl() {
        return url;
    }

    /**
     * @return Type of Activity**/
    public ActivityType getType() {
        return type;
    }

    /**
     * @return Activity name**/
    public String getName() {
        return name;
    }

    /**
     * @param name Activity name
     * @param type Watching, playing, etc...**/
    public Activity(String name, ActivityType type) {
        this.name = name;
        this.type = type;
        this.url = "";
        this.details = "";
        this.state = "";
    }

    /**
     * @param url Stream url
     * @apiNote Using only with type STREAMING**/
    public Activity setUrl(String url) {
        this.url = url;
        return this;
    }

    /**
     * @param details Activity details**/
    public Activity setDetails(String details) {
        this.details = details;
        return this;
    }

    /**
     * @param state Activity state**/
    public Activity setState(String state) {
        this.state = state;
        return this;
    }
}
