package me.beldon.http.handler.impl;

import me.beldon.http.context.Response;
import me.beldon.http.context.impl.DefaultHttpResponse;
import me.beldon.http.handler.ResponseHandler;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 * @create 2018-04-09 下午6:07
 */
public class DefaultResponseHandler implements ResponseHandler {

    @Override
    public Response handle(SocketChannel channel) throws IOException {
        OutputStream outputStream = Channels.newOutputStream(channel);
        return new DefaultHttpResponse(outputStream);
    }

}
