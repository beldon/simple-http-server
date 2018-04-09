package me.beldon.http.server;


import me.beldon.http.handler.RequestHandler;
import me.beldon.http.handler.ResponseHandler;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:21
 */
public class BossContextManager {

    private ExecutorService workPool = Executors.newCachedThreadPool();

    private final RequestHandler requestHandler;
    private final ResponseHandler responseHandler;

    public BossContextManager(RequestHandler requestHandler, ResponseHandler responseHandler) {
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
    }


    public void handle(SocketChannel socketChannel) {
        ClientContext clientContext = new ClientContext(socketChannel, requestHandler, responseHandler);
        workPool.submit(clientContext);
    }


}
