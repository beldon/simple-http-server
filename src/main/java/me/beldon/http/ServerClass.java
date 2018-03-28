package me.beldon.http;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 */
@Component
@Slf4j
public class ServerClass implements ApplicationListener<ContextRefreshedEvent> {

    private int port = 81;
    private Selector selector;

    private volatile boolean running = true;

    private ExecutorService root = Executors.newFixedThreadPool(1);
    private ExecutorService clients = Executors.newCachedThreadPool();


    private void starServer() throws IOException {
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(new InetSocketAddress(port), 150);
        selector = Selector.open();
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("服务器启动成功");
        root.submit(() -> {
            while (running) {
                try {
                    int readyChannels = selector.select();
                    if (readyChannels == 0) {
                        log.info("continue");
                        continue;
                    }
                    Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
                    while (iterator.hasNext()) {
                        log.info("connect");
                        SelectionKey key = iterator.next();
                        iterator.remove();

                        if (key.isAcceptable()) {
                            ServerSocketChannel server = (ServerSocketChannel) key.channel();
                            //得到接收到的SocketChannel
                            SocketChannel socketChannel = server.accept();
                            if(socketChannel != null) {
                                log.info("收到了来自" + ((InetSocketAddress)socketChannel.getRemoteAddress()).getHostString() + "的请求");
                                //将socketChannel设置为阻塞模式
                                socketChannel.configureBlocking(false);
                                //将socketChannel注册到选择器
                                socketChannel.register(selector, SelectionKey.OP_READ);
                            }
                        } else if (key.isReadable()) {
                            //该key有Read事件
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            String requestHeader = "";
                            //拿出通道中的Http头请求
                            try {
                                requestHeader = receive(socketChannel);
                            } catch (Exception e) {
                                log.error("读取socketChannel出错");
                                return;
                            }
                            //启动线程处理该请求,if条件判断一下，防止心跳包
                            if(requestHeader.length() > 0) {
                                log.info("该请求的头格式为\r\n" + requestHeader);
                                log.info("启动了子线程..");
                                new Thread(new HttpHandler(requestHeader, key)).start();
                            }
                        } else if (key.isWritable()) {
                            //该key有Write事件
                            log.info("有流写出!");
                            SocketChannel socketChannel = (SocketChannel) key.channel();
                            socketChannel.shutdownInput();
                            socketChannel.close();
                        }

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private String receive(SocketChannel socketChannel) throws Exception {
        //声明一个1024大小的缓冲区
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        byte[] bytes = null;
        int size = 0;
        //定义一个字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //将socketChannel中的数据写入到buffer中，此时的buffer为写模式，size为写了多少个字节
        while ((size = socketChannel.read(buffer)) > 0) {
            //将写模式改为读模式
            //The limit is set to the current position and then the position is set to zero.
            //将limit设置为之前的position，而将position置为0，更多java nio的知识会写成博客的
            buffer.flip();
            bytes = new byte[size];
            //将Buffer写入到字节数组中
            buffer.get(bytes);
            //将字节数组写入到字节缓冲流中
            baos.write(bytes);
            //清空缓冲区
            buffer.clear();
        }
        //将流转回字节数组
        bytes = baos.toByteArray();
        return new String(bytes);
    }

    private void doAccept(SelectionKey selectionKey) throws IOException {
        ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
        SocketChannel socketChannel;
        while ((socketChannel = serverSocketChannel.accept()) != null) {
            log.info("doAccept accept");
            try {
                socketChannel.configureBlocking(false);
                socketChannel.socket().setTcpNoDelay(true);
                socketChannel.socket().setKeepAlive(true);
            } catch (IOException e) {
                socketChannel.close();
                throw e;
            }
            ByteBuffer buffer = ByteBuffer.allocate(10);
            buffer.put("hello".getBytes());
            buffer.flip();
//            buffer.rewind();
            socketChannel.write(buffer);
        }
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        try {
            starServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
