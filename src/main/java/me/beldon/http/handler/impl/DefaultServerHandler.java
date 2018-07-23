package me.beldon.http.handler.impl;

import me.beldon.http.constant.CommonConstant;
import me.beldon.http.context.Request;
import me.beldon.http.context.Response;
import me.beldon.http.handler.ServerHandler;
import me.beldon.http.util.ImageCheck;

import java.io.*;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;
import java.util.Date;

public class DefaultServerHandler implements ServerHandler {

    private File root = new File("html");
    private static String DEFAULT_CONTENT_TYPE = "text/html; charset=UTF-8";


    @Override
    public void handler(Request request, Response response) throws Exception {
        String uri = request.getUri();
        response.setContentType(DEFAULT_CONTENT_TYPE);
        if (uri.endsWith("/")) {
            uri = uri + "index.html";
        }
        File targetFile = new File(root, uri);
        if (!targetFile.exists()) {
            response.setHttpStatus(404);
            response.setContentType("text/html; charset=UTF-8");
            response.setContentLength("not found".length());
            writeHeader(response);
            writeContent(response, "not found");
        } else {
            if (ImageCheck.isImage(targetFile)) {
                response.setContentType("image/png");
            }
            response.setContentLength(targetFile.length());
            writeHeader(response);
            writeContent(response, targetFile);
        }
    }

    private void writeHeader(Response response) {
        OutputStream outputStream = response.getOutputStream();
        StringBuilder sb = new StringBuilder();
        sb.append(CommonConstant.PROTOCOL).append(" ").append(response.getHttpStatus()).append("\r\n");
        sb.append("Server: bel\r\n");
        sb.append("Content-Type: ").append(response.getContentType()).append("\r\n");

        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Content-Length: " + response.getContentLength() + "\r\n");

        //响应内容
        sb.append("\r\n");
        try {
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeContent(Response response, String content) {
        OutputStream outputStream = response.getOutputStream();
        try {
            outputStream.write(content.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void writeContent(Response response, File file) {
        WritableByteChannel channel = Channels.newChannel(response.getOutputStream());
        try (
                FileInputStream fis = new FileInputStream(file)
        ) {
            fis.getChannel().transferTo(0, file.length(), channel);
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
