/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.api.lowLevel;

import com.google.gson.JsonObject;
import net.sascha123789.djava.utils.Constants;
import net.sascha123789.djava.utils.ErrHandler;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class DiscordHttp {
    private HttpClient client;
    private String token;

    public DiscordHttp(String token) {
        this.token = token;
        this.client = HttpClient.newBuilder()
                .build();
    }

    public HttpPacket doPatch(String contentType, String endpoint, String data) {
        HttpRequest request = HttpRequest.newBuilder()
                .method("PATCH", HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(Constants.BASE_URL + endpoint))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + token)
                .header("Content-Type", contentType)
                .build();
        StringBuilder res = new StringBuilder();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject o = null;
        try {
            o = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        } catch(Exception ignored) {

        }

        return new HttpPacket(res.toString(), o);
    }

    public HttpPacket doPut(String contentType, String endpoint, String data) {
        HttpRequest request = HttpRequest.newBuilder()
                .PUT(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(Constants.BASE_URL + endpoint))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + token)
                .header("Content-Type", contentType)
                .build();
        StringBuilder res = new StringBuilder();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject o = null;
        try {
            o = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        } catch(Exception ignored) {

        }

        return new HttpPacket(res.toString(), o);
    }

    public HttpPacket doPost(String contentType, String endpoint, String data) {
        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(data))
                .uri(URI.create(Constants.BASE_URL + endpoint))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + token)
                .header("Content-Type", contentType)
                .build();
        StringBuilder res = new StringBuilder();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject o = null;

        try {
            o = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        } catch(Exception ignored) {

        }

        return new HttpPacket(res.toString(), o);
    }

    public HttpPacket doDelete(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .DELETE()
                .uri(URI.create(Constants.BASE_URL + endpoint))
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + token)
                .build();
        StringBuilder res = new StringBuilder();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject o = null;
        try {
            o = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        } catch(Exception ignored) {

        }

        return new HttpPacket(res.toString(), o);
    }

    public HttpPacket doGet(String endpoint) {
        HttpRequest request = HttpRequest.newBuilder()
                .GET()
                .header("User-Agent", Constants.USER_AGENT)
                .header("Authorization", "Bot " + token)
                .uri(URI.create(Constants.BASE_URL + endpoint))
                .build();
        StringBuilder res = new StringBuilder();

        client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(content -> {
                    ErrHandler.handle(content);
                    res.append(content);
                }).join();

        JsonObject o = null;
        try {
             o = Constants.GSON.fromJson(res.toString(), JsonObject.class);
        } catch(Exception ignored) {

        }

        return new HttpPacket(res.toString(), o);
    }
}
