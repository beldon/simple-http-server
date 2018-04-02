package me.beldon.http.server;


import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:21
 */
public class ContextManager {

    private ExecutorService workPool = Executors.newCachedThreadPool();


    public void handle(SocketChannel socketChannel) {
        ClientContext clientContext = new ClientContext();
        clientContext.setSocketChannel(socketChannel);
        workPool.submit(clientContext);
    }


}
