package me.beldon.http.handler;

import me.beldon.http.context.Response;

import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 */
public interface ResponseHandler extends Handler<SocketChannel, Response> {


}
