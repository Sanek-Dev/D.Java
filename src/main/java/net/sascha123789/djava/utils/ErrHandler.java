package net.sascha123789.djava.utils;

import com.google.gson.JsonObject;

public class ErrHandler {
    public static void handle(String str) {
        JsonObject o = null;

            try {
                o = Constants.GSON.fromJson(str, JsonObject.class);
            } catch(Exception ignored) {
                return;
            }

            if(o == null) {
                return;
            }

            if (o.has("code") && o.has("message")) {
                int code = o.get("code").getAsInt();
                String msg = o.get("message").getAsString();

                if(msg.equals("404: Not Found")) {
                    return;
                }

                throw new DiscordAPIException("API Error!\nError code: " + code + "\nMessage: " + msg);
            }
    }
}
