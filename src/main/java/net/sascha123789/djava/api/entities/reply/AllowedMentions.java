/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.entities.reply;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sascha123789.djava.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class AllowedMentions {
    private List<String> parse;
    private List<String> roles;
    private List<String> users;
    private boolean repliedUser;

    private AllowedMentions(List<String> parse, List<String> roles, List<String> users, boolean repliedUser) {
        this.parse = parse;
        this.roles = roles;
        this.users = users;
        this.repliedUser = repliedUser;
    }

    public final JsonNode toJson() {
        ObjectNode o = Constants.MAPPER.createObjectNode();
        ArrayNode arrParse = Constants.MAPPER.createArrayNode();

        for(String el: parse) {
            arrParse.add(el);
        }

        ArrayNode arrRoles = Constants.MAPPER.createArrayNode();

        for(String el: roles) {
            arrRoles.add(el);
        }

        ArrayNode arrUsers = Constants.MAPPER.createArrayNode();
        for(String el: users) {
            arrUsers.add(el);
        }

        if(!roles.isEmpty()) {
            o.set("roles", arrRoles);
        }

        if(!users.isEmpty()) {
            o.set("users", arrUsers);
        }

        o.set("parse", arrParse);
        o.put("replied_user", repliedUser);

        return o;
    }

    public static AllowedMentions create(List<Type> parseTypes) {
        List<String> parse = new ArrayList<>();

        for(Type type: parseTypes) {
            parse.add((type == Type.ROLES ? "roles" : (type == Type.USERS ? "users" : "everyone")));
        }

        return new AllowedMentions(parse, new ArrayList<>(), new ArrayList<>(), false);
    }

    public AllowedMentions setRepliedUser(boolean repliedUser) {
        this.repliedUser = repliedUser;
        return this;
    }

    public AllowedMentions addUser(String id) {
        users.add(id);
        return this;
    }

    public AllowedMentions addRole(String id) {
        roles.add(id);
        return this;
    }

    public static enum Type {
        ROLES, USERS, EVERYONE_AND_HERE;
    }
}
