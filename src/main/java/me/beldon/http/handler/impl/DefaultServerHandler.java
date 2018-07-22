package me.beldon.http.handler.impl;

import me.beldon.http.constant.CommonConstant;
import me.beldon.http.context.Request;
import me.beldon.http.context.Response;
import me.beldon.http.handler.ServerHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;

public class DefaultServerHandler implements ServerHandler {
    @Override
    public void handler(Request request, Response response) throws Exception {
        response(response);
    }

    private void response(Response response) {
        OutputStream outputStream = response.getOutputStream();
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
        try {
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        ByteBuffer buffer = ByteBuffer.allocate(1024);
//        buffer.put(sb.toString().getBytes());
//        buffer.flip();
//        try {
//            log.info("生成相应\r\n" + sb.toString());
//            socketChannel.write(buffer);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
