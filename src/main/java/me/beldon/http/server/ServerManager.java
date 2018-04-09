package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.config.CommonConfig;
import me.beldon.http.handler.RequestHandler;
import me.beldon.http.handler.ResponseHandler;
import me.beldon.http.handler.impl.DefaultRequestHandler;
import me.beldon.http.handler.impl.DefaultResponseHandler;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 */
@Slf4j
public class ServerManager {
    private final ExecutorService bossPool = Executors.newFixedThreadPool(1);
    private CommonConfig commonConfig;


    private final BossContextManager contextManager;
    private final BossContext bossContext;

    private final RequestHandler requestHandler;
    private final ResponseHandler responseHandler;


    public ServerManager() {
        commonConfig = new CommonConfig();
        requestHandler = new DefaultRequestHandler();
        responseHandler = new DefaultResponseHandler();
        contextManager = new BossContextManager(requestHandler, responseHandler);
        bossContext = new BossContext(contextManager, commonConfig.getPort());
    }


    public void destroy() {
        bossContext.stop();
    }

    public void startServer() throws Exception {
        bossContext.start();
        bossPool.submit(bossContext);
    }
}
