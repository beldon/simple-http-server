package me.beldon.http.handler;

import me.beldon.http.context.Request;

import java.nio.channels.SocketChannel;

/**
 * @author Beldon
 */
public interface RequestHandler extends Handler<SocketChannel, Request> {

}
