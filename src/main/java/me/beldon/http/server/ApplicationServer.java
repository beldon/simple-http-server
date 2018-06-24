package me.beldon.http.server;

import java.io.IOException;

public interface ApplicationServer {
    /**
     * 启动服务器
     */
    void start() throws IOException;

    /**
     * 停止服务器
     */
    void stop();
}
