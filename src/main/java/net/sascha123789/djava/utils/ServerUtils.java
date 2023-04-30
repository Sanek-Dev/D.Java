/*
 * Copyright (c) Sascha123789 2023.
 */

package net.sascha123789.djava.utils;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class ServerUtils {
    private ServerUtils() {

    }

    public static void startServer() {
        try {
            HttpServer server = HttpServer.create(new InetSocketAddress(443), 0);
            server.createContext("/", new HttpHandler() {
                @Override
                public void handle(HttpExchange exchange) throws IOException {
                    String res = "Server started successfully!";
                    exchange.sendResponseHeaders(200, res.getBytes().length);
                    OutputStream os = exchange.getResponseBody();
                    os.write(res.getBytes());
                    os.close();
                }
            });
            server.setExecutor(null);
            server.start();
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
