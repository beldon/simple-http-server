package me.beldon.http;

import me.beldon.http.server.ServerManager;

/**
 * @author beldon
 */
public class LaunchApplication {

    public static void main(String[] args) throws Exception {
        new ServerManager().startServer();
    }
}
