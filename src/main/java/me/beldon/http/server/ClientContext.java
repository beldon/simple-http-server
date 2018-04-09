package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.context.CommonContextHolder;
import me.beldon.http.context.Request;
import me.beldon.http.handler.RequestHandler;
import me.beldon.http.handler.ResponseHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:18
 */
@Slf4j
public class ClientContext implements Runnable {

    private final SocketChannel socketChannel;
    private final RequestHandler requestHandler;
    private final ResponseHandler responseHandler;

    public ClientContext(SocketChannel socketChannel, RequestHandler requestHandler, ResponseHandler responseHandler) {
        this.socketChannel = socketChannel;
        this.requestHandler = requestHandler;
        this.responseHandler = responseHandler;
    }


    @Override
    public void run() {
        try {
            if (socketChannel != null) {
                log.info("收到了来自" + ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString() + "的请求");
                //将socketChannel设置为阻塞模式
                socketChannel.configureBlocking(false);
                Request request = requestHandler.handle(socketChannel);
                CommonContextHolder.RequestHolder.set(request);
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("请求异常", e);
        } finally {
            CommonContextHolder.RequestHolder.remove();
        }
    }


}
