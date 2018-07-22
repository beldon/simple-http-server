package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.config.HttpConfig;
import me.beldon.http.config.HttpConfigurer;
import me.beldon.http.context.Request;
import me.beldon.http.context.Response;
import me.beldon.http.handler.RequestHandler;
import me.beldon.http.handler.ResponseHandler;
import me.beldon.http.handler.ServerHandler;
import me.beldon.http.handler.impl.DefaultRequestHandler;
import me.beldon.http.handler.impl.DefaultResponseHandler;
import me.beldon.http.handler.impl.DefaultServerHandler;

import java.io.IOException;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public class DefaultHttpServer extends AbstractHttpServer {

    private final HttpConfig config;

    private RequestHandler requestHandler;

    private ResponseHandler responseHandler;

    private ServerHandler serverHandler;

    public DefaultHttpServer(HttpConfigurer configurer) {
        super(configurer);
        this.config = super.getConfig();

        responseHandler = new DefaultResponseHandler();
        requestHandler = new DefaultRequestHandler();
        serverHandler = new DefaultServerHandler();
    }

    @Override
    protected Selector selector() {
        try {
            return Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected ExecutorService handlerExecutor() {
        return Executors.newFixedThreadPool(getConfig().getConnectThreads());
    }

    @Override
    protected void handlerConnect(SocketChannel channel) {
        log.info("client connect");
        try {
            Request request = null;
//            Request request = requestHandler.handle(channel);
            Response response = responseHandler.handle(channel);
            serverHandler.handler(request, response);
        } catch (Exception e) {
            log.error("handle connect error,", e);
        }
    }
}
