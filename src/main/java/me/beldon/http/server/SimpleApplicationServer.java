package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

@Slf4j
public class SimpleApplicationServer implements ApplicationServer {

    private final Integer port;
    private volatile boolean running;

    private Selector selector;


    public SimpleApplicationServer(Integer port) {
        this.port = port;
    }

    @Override
    public void start() throws IOException {
        selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port), 150);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务器启动成功，端口:{}", port);
        running = true;
        accept();
    }

    @Override
    public void stop() {

    }


    private void accept() throws IOException {
        while (running) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                log.info("continue");
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    running = false;
                    break;
                }
                continue;
            }
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();

                if (key.isAcceptable()) {
                    ServerSocketChannel server = (ServerSocketChannel) key.channel();
                    SocketChannel socketChannel = server.accept();
                }
            }
        }
    }
}
