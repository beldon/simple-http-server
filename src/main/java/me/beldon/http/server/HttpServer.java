package me.beldon.http.server;

/**
 * http服务器
 */
public interface HttpServer {
    void start() throws Exception;

    void stop() throws Exception;
}
