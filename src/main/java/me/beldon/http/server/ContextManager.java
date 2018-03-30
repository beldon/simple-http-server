package me.beldon.http.server;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.nio.channels.SocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:21
 */
@Component
public class ContextManager implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    private ExecutorService workPool = Executors.newCachedThreadPool();


    public void handle(SocketChannel socketChannel) {
        ClientContext clientContext = applicationContext.getBean(ClientContext.class);
        clientContext.setSocketChannel(socketChannel);
        workPool.submit(clientContext);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
