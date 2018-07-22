package me.beldon.http;

import me.beldon.http.config.HttpConfigurer;
import me.beldon.http.server.DefaultHttpServer;

/**
 * @author beldon
 */
public class LaunchApplication {

    public static void main(String[] args) throws Exception {
        DefaultHttpServer httpServer = new DefaultHttpServer(configurer());
        httpServer.start();
    }

    private static HttpConfigurer configurer() {
        return config -> {

        };
    }
}
