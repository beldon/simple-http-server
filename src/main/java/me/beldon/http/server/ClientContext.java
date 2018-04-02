package me.beldon.http.server;

import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Date;

/**
 * @author Beldon
 * @create 2018-03-30 下午4:18
 */
@Slf4j
public class ClientContext implements Runnable {

    private Selector selector;

    private SocketChannel socketChannel;

    public void setSocketChannel(SocketChannel socketChannel) {
        this.socketChannel = socketChannel;
        try {
            this.selector = Selector.open();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            if (socketChannel != null) {
                log.info("收到了来自" + ((InetSocketAddress) socketChannel.getRemoteAddress()).getHostString() + "的请求");
                //将socketChannel设置为阻塞模式
                socketChannel.configureBlocking(false);
                //将socketChannel注册到选择器
                socketChannel.register(selector, SelectionKey.OP_READ);
                handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void handle() throws IOException {
        String header = getHeader(socketChannel);
        log.info("header:{}", header);
        writeImg();

//        File file = new File("html/temp.html");
//        FileInputStream fileInputStream = new FileInputStream(file);
//        String html = StreamUtils.copyToString(fileInputStream, StandardCharsets.UTF_8);
//        write(socketChannel, html);
        socketChannel.shutdownInput();
        socketChannel.close();
    }


    public String getHeader(SocketChannel socketChannel) throws IOException {
        log.info("header------");
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

    public void write(SocketChannel socketChannel, String html) {
        log.info("write Content");
        //得到响应正文内容
//        String html = "hello world";

        StringBuilder sb = new StringBuilder();
        //状态行
        sb.append("HTTP/1.1 200 OK\r\n");
        //响应头
        sb.append("Server: bel\r\n");
        sb.append("Content-Type: text/html; charset=UTF-8\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Content-Length: " + html.getBytes().length + "\r\n");

        //响应内容
        sb.append("\r\n");
        sb.append(html);
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sb.toString().getBytes());
        //从写模式，切换到读模式
        buffer.flip();
        try {
            log.info("生成相应\r\n" + sb.toString());
//            socketChannel.register(selector, SelectionKey.OP_WRITE);
            socketChannel.write(buffer);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void writeImg() throws IOException {
        File img = new File("html/temp.png");
        long length = img.length();

        StringBuilder sb = new StringBuilder();
        //状态行
        sb.append("HTTP/1.1 200 OK\r\n");
        //响应头
        sb.append("Server: bel\r\n");
        sb.append("Content-Type: image/jpeg\r\n");
        sb.append("Date: " + new Date() + "\r\n");
        sb.append("Content-Length: " + length + "\r\n");

        //响应内容
        sb.append("\r\n");
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.put(sb.toString().getBytes());
        buffer.flip();
        socketChannel.write(buffer);
        buffer.clear();
        FileChannel fileChannel = new FileInputStream(img).getChannel();
        while (fileChannel.read(buffer) != -1) {
            fileChannel.read(buffer);
            buffer.flip();
            socketChannel.write(buffer);
            buffer.clear();
        }
    }
}
