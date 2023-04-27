/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.ipc;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.sascha123789.djava.api.User;
import net.sascha123789.djava.ipc.entities.Activity;
import net.sascha123789.djava.ipc.enums.DiscordVariant;
import net.sascha123789.djava.ipc.enums.IPCStatus;
import net.sascha123789.djava.ipc.enums.IPCType;
import net.sascha123789.djava.utils.Constants;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.management.ManagementFactory;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;

public class DiscordIPC {
    private String clientId;
    private RandomAccessFile file;
    private IPCStatus status = IPCStatus.CONNECTING;
    private DiscordVariant build;
    private IPCEventListener ipcListener;

    private DiscordIPC(String clientId, String loc) {
        this.clientId = clientId;

        try {
            this.file = new RandomAccessFile(new File(loc), "rw");
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public IPCPackage readFromPipe() {
        try {
            while(file.length() == 0 && status == IPCStatus.CONNECTED) {
                Thread.sleep(50);
            }

            IPCType op = IPCType.values()[Integer.reverseBytes(file.readInt())];
            int len = Integer.reverseBytes(file.readInt());
            byte[] arr = new byte[len];

            file.readFully(arr);
            IPCPackage ipcPackage = new IPCPackage(op, Constants.GSON.fromJson(new String(arr), JsonObject.class));

            return ipcPackage;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void writeToPipe(IPCType op, JsonObject data) {
        try {
            String nonce = UUID.randomUUID().toString();
            data.addProperty("nonce", nonce);

            IPCPackage ipcPackage = new IPCPackage(op, data);
            file.write(ipcPackage.toByteArr());

            if(ipcListener != null) {
                ipcListener.onPackageSent(ipcPackage);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    private static int getPID()
    {
        String pr = ManagementFactory.getRuntimeMXBean().getName();
        return Integer.parseInt(pr.substring(0,pr.indexOf('@')));
    }

    public String auth(String clientSecret) {
        JsonObject root = new JsonObject();
        root.addProperty("cmd", "AUTHORIZE");
        JsonObject args = new JsonObject();
        args.addProperty("client_id", clientId);
        JsonArray scopes = new JsonArray();
        scopes.add("rpc");
        scopes.add("identify");
        args.add("scopes", scopes);
        root.add("args", args);

        writeToPipe(IPCType.FRAME, root);

        IPCPackage pack = readFromPipe();
        String code = pack.getData().get("data").getAsJsonObject().get("code").getAsString();

        JsonObject obj = new JsonObject();
        obj.addProperty("client_id", clientId);
        obj.addProperty("client_secret", clientSecret);
        obj.addProperty("grant_type", "authorization_code");
        obj.addProperty("code", code);
        obj.addProperty("redirect_uri", "https://127.0.0.1");
        StringBuilder b = new StringBuilder();
        b.append("client_id=" + clientId + "&client_secret=" + clientSecret + "&grant_type=authorization_code&code=" + code + "&redirect_uri=https://127.0.0.1");

        HttpRequest request = HttpRequest.newBuilder()
                .POST(HttpRequest.BodyPublishers.ofString(b.toString()))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .uri(URI.create(Constants.BASE_URL + "/oauth2/token"))
                .build();
        StringBuilder resStr = new StringBuilder();

        HttpClient.newBuilder().build().sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(HttpResponse::body)
                .thenAccept(resStr::append).join();

        JsonObject res = Constants.GSON.fromJson(resStr.toString(), JsonObject.class);
        String accessToken = res.get("access_token").getAsString();

        JsonObject auth = new JsonObject();
        auth.addProperty("cmd", "AUTHENTICATE");
        JsonObject argsAuth = new JsonObject();
        argsAuth.addProperty("access_token", accessToken);
        auth.add("args", argsAuth);

        writeToPipe(IPCType.FRAME, auth);

        IPCPackage pack1 = readFromPipe();
        JsonObject data = pack1.getData().get("data").getAsJsonObject();
        //System.out.println("Content: " + pack1.getData().toString());
        User user = Constants.GSON.fromJson(data.get("user").getAsJsonObject(), User.class);

        ipcListener.onAuth(this, user, accessToken);

        return code;
    }

    public void setPresence(Activity activity) {
        JsonObject data = activity.toJson();

        JsonObject root = new JsonObject();
        root.addProperty("cmd", "SET_ACTIVITY");
        JsonObject args = new JsonObject();
        args.add("activity", data);
        args.addProperty("pid", getPID());
        root.add("args", args);

        writeToPipe(IPCType.FRAME, root);
    }

    public static DiscordIPC connectToPipe(String clientId, IPCEventListener listener) {
        DiscordIPC ipc = null;
        for(int i = 0; i < 10; i++) {
            try {
                String loc = getPipeLocation(i);
                ipc = new DiscordIPC(clientId, loc);
                JsonObject obj = new JsonObject();
                obj.addProperty("v", 1);
                obj.addProperty("client_id", clientId);

                ipc.writeToPipe(IPCType.HANDSHAKE, obj);

                IPCPackage ipcPackage = ipc.readFromPipe();
                String endpoint = ipcPackage.getData().get("data").getAsJsonObject().get("config").getAsJsonObject().get("api_endpoint").getAsString();
                ipc.build = (Objects.equals(endpoint, "//discordapp.com/api") ? DiscordVariant.STABLE : (endpoint.equals("//ptb.discordapp.com/api") ? DiscordVariant.PTB : DiscordVariant.CANARY));

                ipc.status = IPCStatus.CONNECTED;
                ipc.ipcListener = listener;

                new Thread(() -> {
                    try {
                        CountDownLatch latch = new CountDownLatch(1);
                        latch.await();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                listener.onReady(ipc);

                return ipc;
            } catch(Exception e) {
                ipc = null;
                break;
            }
        }

        return null;
    }

    private static String getPipeLocation(int i)
    {
            return "\\\\?\\pipe\\discord-ipc-"+i;
    }
}
