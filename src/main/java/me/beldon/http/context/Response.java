package me.beldon.http.context;

import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * @author Beldon
 */
public interface Response {
    String getContentType();

    OutputStream getOutputStream();

    PrintWriter getWriter();

    void setCharacterEncoding(String charset);

    void setContentLength(long len);

    long getContentLength();

    void setContentType(String contentType);

    void setHeader(String name, String value);

    String getHeader(String name);

    void setHttpStatus(int code);

    int getHttpStatus();
}
