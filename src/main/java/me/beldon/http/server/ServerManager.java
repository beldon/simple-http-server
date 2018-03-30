package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.config.CommonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:06
 */
@Component
@Slf4j
public class ServerManager implements ApplicationListener<ContextRefreshedEvent> {
    private final ExecutorService bossPool = Executors.newFixedThreadPool(1);

    @Autowired
    private CommonConfig commonConfig;

    @Autowired
    private ContextManager contextManager;

    private Selector selector;

    private BossContext bossContext;

    @PreDestroy
    public void destroy() {
        if (bossContext != null) {
            bossContext.stop();
        }
    }

    public void startServer() throws Exception {
        int port = commonConfig.getPort();
        selector = Selector.open();
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port), 150);
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务器启动成功，端口:{}", port);
        bossContext = new BossContext(selector, contextManager);
        bossPool.submit(bossContext);
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            startServer();
        } catch (Exception e) {
            e.printStackTrace();
            log.error("服务启动失败", e);
        }
    }
}
