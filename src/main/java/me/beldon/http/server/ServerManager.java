package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.config.CommonConfig;
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
@Slf4j
public class ServerManager{
    private final ExecutorService bossPool = Executors.newFixedThreadPool(1);

    private CommonConfig commonConfig;

    private ContextManager contextManager;

    private Selector selector;

    private BossContext bossContext;


    public ServerManager() {
        commonConfig = new CommonConfig();
        contextManager = new ContextManager();
    }


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

}
