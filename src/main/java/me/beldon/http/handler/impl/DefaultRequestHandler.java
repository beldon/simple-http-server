package me.beldon.http.handler.impl;

import me.beldon.http.context.Request;
import me.beldon.http.context.impl.HttpRequest;
import me.beldon.http.handler.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 * @create 2018-04-09 下午6:05
 */
public class DefaultRequestHandler implements RequestHandler {
    /**
     * 换行符
     */
    private static final int LF = 10;
    /**
     * 回车符
     */
    private static final int CR = 13;

    @Override
    public Request handle(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        ByteArrayOutputStream headers = new ByteArrayOutputStream();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        boolean hasHeader = true;
        boolean flag = false;
        byte lastByte = -1;
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                if (hasHeader) {
                    headers.write(b);
                    if (b == CR || b == LF) {
                        if (lastByte == CR && b == LF) {
                            if (flag) {
                                hasHeader = false;
                                continue;
                            }
                            flag = true;
                        }
                    } else {
                        flag = false;
                    }
                } else {
                    body.write(b);
                }
                lastByte = b;
            }
            buffer.clear();
        }
        String remoteAddress = ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString();
        return new HttpRequest(new String(headers.toByteArray()), body.toByteArray(),remoteAddress);
    }
}
