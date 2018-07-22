package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.config.HttpConfigurer;
import me.beldon.http.config.HttpConfig;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
public abstract class AbstractHttpServer implements HttpServer {
    private final ExecutorService bossPool = Executors.newFixedThreadPool(1);

    private final HttpConfigurer configurer;

    private final HttpConfig config = new HttpConfig();

    private ExecutorService handlerExecutor;

    private Selector selector;

    private boolean running;

    protected AbstractHttpServer(HttpConfigurer configurer) {
        this.configurer = configurer;
        configurer.configure(config);
        init();
    }


    @Override
    public final void start() throws IOException {
        running = true;
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(config.getHttpPort()), config.getServerBacklog());
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务器启动成功，端口:{}", config.getHttpPort());
        bossPool.submit(new Worker());
    }

    @Override
    public final void stop() {
        running = false;
    }


    private void init() {
        this.selector = selector();
        this.handlerExecutor = handlerExecutor();
    }


    /**
     * selector
     *
     * @return
     */
    protected abstract Selector selector();

    /**
     * handler executor
     *
     * @return
     */
    protected abstract ExecutorService handlerExecutor();

    /**
     * 处理connect
     *
     * @param channel
     */
    protected abstract void handlerConnect(SocketChannel channel);


    private class Worker implements Runnable {

        private final Selector selector;

        private Worker() {
            this.selector = AbstractHttpServer.this.selector;
        }


        @Override
        public void run() {
            try {
                doServer();
            } catch (IOException e) {
                log.error("服务器异常，", e);
            }
        }

        private void doServer() throws IOException {
            while (running) {
                selector.select();
                Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                while (iterator.hasNext()) {
                    SelectionKey key = iterator.next();
                    iterator.remove();
                    if (key.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel) key.channel();
                        SocketChannel socketChannel = server.accept();
                        AbstractHttpServer.this.handlerExecutor.submit(() -> handlerConnect(socketChannel));
                    }
                }
            }
        }
    }

    public boolean isRunning() {
        return running;
    }

    public HttpConfigurer getConfigurer() {
        return configurer;
    }

    public HttpConfig getConfig() {
        return config;
    }
}

