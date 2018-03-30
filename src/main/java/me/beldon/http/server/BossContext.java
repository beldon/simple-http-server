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

/**
 * @author Beldon
 */
@Slf4j
public class BossContext implements Runnable {

    private final Selector selector;
    private volatile boolean running;

    private final ContextManager contextManager;

    public BossContext(Selector selector, ContextManager contextManager) {
        this.selector = selector;
        this.contextManager = contextManager;
        running = true;
    }

    @Override
    public void run() {
        try {
            select();
        } catch (IOException e) {
            e.printStackTrace();
            log.error("服务器运行异常", e);
        }
    }

    private void select() throws IOException {
        while (running) {
            int readyChannels = selector.select();
            if (readyChannels == 0) {
                log.info("continue");
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
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
                    contextManager.handle(socketChannel);
                }
            }
        }
    }

    public void stop() {
        running = false;
    }

}
