package demo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.StringReader;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author Beldon
 * @create 2018-04-09 下午5:14
 */
@SuppressWarnings("Duplicates")
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


    private byte crlf13 = (byte) 13; //碶 ?br/>
    private byte crlf10 = (byte) 10;  //碶 ?/FONT>

    private StringBuffer request = new StringBuffer();  //用于保存所有内容

    @Test
    public void read() throws Exception {
        String targetFile = "html/demo.txt";

        FileInputStream fis = new FileInputStream(targetFile);

        byte[] crlf = new byte[1];
        int crlfNum = 0;   //已经连接的回车换行数 crlfNum=4为头部结束
        while (fis.read(crlf) != -1)   //读取头部
        {
            if (crlf[0] == crlf10) {
                crlfNum++;
            } else {
                crlfNum = 0;

            }
            request.append(new String(crlf, 0, 1));  //byte数组相加
            if (crlfNum == 2) {
                break;
            }
        }
        System.out.println(request.toString());
        System.out.println("----");


        StringReader reader = new StringReader(request.toString());
        BufferedReader bufferedReader = new BufferedReader(reader);
        System.out.println(bufferedReader.readLine());

    }


    @Test
    public void handleRequest() throws Exception {
        String targetFile = "html/temp.txt";

        FileInputStream fis = new FileInputStream(targetFile);

        byte[] data = new byte[1];
        while (fis.read(data) != -1) {
            System.out.println(data[0]);
        }
    }

}
