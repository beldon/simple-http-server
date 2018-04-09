package me.beldon.http.handler.impl;

import me.beldon.http.context.Request;
import me.beldon.http.handler.RequestHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 * @create 2018-04-09 下午6:05
 */
public class DefaultRequestHandler implements RequestHandler {
    @Override
    public Request handle(SocketChannel socketChannel) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(20);
        ByteArrayOutputStream headers = new ByteArrayOutputStream();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        boolean flag = false;
        boolean hasHeader = true;
        while (socketChannel.read(buffer) > 0) {
            buffer.flip();
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                if (hasHeader) {
                    if (b == 10 || b == 13) {// 换行或回车
                        if (flag) {
                            hasHeader = false;
                            continue;
                        }
                        flag = true;
                    } else {
                        flag = false;
                    }
                    headers.write(b);
                }else{
                    body.write(b);
                }
            }
            buffer.clear();
        }

        System.out.println(new String(headers.toByteArray()));
        System.out.println("-----------------");
        System.out.println(new String(body.toByteArray()));
        System.out.println("-----------------");
        return null;
    }
}
