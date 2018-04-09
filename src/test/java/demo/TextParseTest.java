package demo;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
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
            while (buffer.hasRemaining()) {
                byte b = buffer.get();
                if (hasHeader) {
                    if (b == 10 || b == 13) {// 换行或回车
                        if (flag) {
                            hasHeader = false;
                            continue;
                        }
                        flag = true;
                    } else {
                        flag = false;
                    }
                    headers.write(b);
                }else{
                    body.write(b);
                }
            }
            buffer.clear();
        }

        System.out.println(new String(headers.toByteArray()));
        System.out.println("-----------------");
        System.out.println(new String(body.toByteArray()));
        System.out.println("-----------------");
    }
}
