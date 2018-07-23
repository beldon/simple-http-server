package me.beldon.http.handler.impl;

import me.beldon.http.context.Request;
import me.beldon.http.context.impl.HttpRequest;
import me.beldon.http.handler.RequestHandler;
import me.beldon.http.util.StringUtils;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Beldon
 * @create 2018-04-09 下午6:05
 */
public class DefaultRequestHandler implements RequestHandler {

    private static final String SPILD = " ";

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
        InputStream is = Channels.newInputStream(socketChannel);
        StringBuffer request = new StringBuffer();
        byte[] buffer = new byte[1];
        int lastByte = -1;
        boolean has = false;

        while (is.read(buffer) != -1)   //读取头部
        {
            request.append(new String(buffer, 0, 1));
            if (buffer[0] == LF && lastByte == CR) {
                if (has) {
                    break;
                }
                has = true;
            }
            if (buffer[0] == CR) {
                lastByte = CR;
            } else {
                if (buffer[0] != LF) {
                    has = false;
                }
                lastByte = -1;
            }


        }

        StringReader reader = new StringReader(request.toString());
        BufferedReader bufferedReader = new BufferedReader(reader);

        String msg = bufferedReader.readLine();
        String[] split = msg.split(SPILD);
        String method = split[0];
        String uri = split[1];
        String protocol = split[2];

        Map<String, String> headers = new HashMap<>();
        String header;
        while (StringUtils.hasText(header = bufferedReader.readLine())) {
            String[] headSpild = header.split(SPILD);
            headers.put(headSpild[0], headSpild[1]);
        }

        String remoteAddress = ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString();
        return new HttpRequest(headers, remoteAddress, uri, protocol, method, is);
    }
}
