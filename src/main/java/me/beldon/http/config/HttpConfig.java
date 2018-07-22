package me.beldon.http.config;

import lombok.Data;

/**
 * @author Beldon
 * @create 2018-07-12 18:17
 */
@Data
public class HttpConfig {

    /**
     * 服务器端口
     */
    private int httpPort = 9999;

    private int serverBacklog = 50;

    /**
     * 连接数
     */
    private int connectThreads = 5;

}
