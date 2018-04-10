package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.constant.CommonConstant;
import me.beldon.http.context.CommonContextHolder;
import me.beldon.http.context.Request;
import me.beldon.http.handler.RequestHandler;
import me.beldon.http.handler.ResponseHandler;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Date;

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
                response();
            }
        } catch (IOException e) {
            e.printStackTrace();
            log.error("请求异常", e);
        } finally {
            CommonContextHolder.RequestHolder.remove();
            close();
        }
    }

    private void response() {
        String content = "hello world";
        StringBuilder sb = new StringBuilder();
        sb.append(CommonConstant.PROTOCOL).append(" 200 OK\r\n");
        sb.append("Server: bel\r\n");
        sb.append("Content-Type: text/html; charset=UTF-8\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Content-Length: " + content.getBytes().length + "\r\n");

        //响应内容
        sb.append("\r\n");
        sb.append(content);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sb.toString().getBytes());
        buffer.flip();
        try {
            log.info("生成相应\r\n" + sb.toString());
            socketChannel.write(buffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void close() {
        try {
            socketChannel.shutdownInput();
            socketChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
