package demo;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Beldon
 * @create 2018-04-09 下午5:14
 */
public class TextParseTest {
    private static final int LF = 10;
    private static final int CR = 13;
    @Test
    public void test() throws Exception {
        FileInputStream fileInputStream = new FileInputStream("html/demo.txt");
        FileChannel channel = fileInputStream.getChannel();
        ByteBuffer buffer = ByteBuffer.allocate(20);
        ByteArrayOutputStream headers = new ByteArrayOutputStream();
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        boolean flag = false;
        boolean hasHeader = true;
        while (channel.read(buffer) > 0) {
            buffer.flip();
            byte lastByte = -1;
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                System.out.println(b);
                if (hasHeader) {
                    if (lastByte == CR && b == LF) {
                        if (flag) {
                            hasHeader = false;
                            System.out.println("--------------hasHeader---------------");
                            continue;
                        }
                        flag = true;
                    } else {
                        if (!(flag && lastByte == LF)) {
                            flag = false;
                        }
                    }
                    headers.write(b);
                } else {
                    body.write(b);
                }
                lastByte = b;
            }
            buffer.clear();
        }

        System.out.println(new String(headers.toByteArray()));
        System.out.println("-----------------");
        System.out.println(new String(body.toByteArray()));
        System.out.println("-----------------");
    }



}
