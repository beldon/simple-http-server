package me.beldon.http.context;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;

/**
 * @author Beldon
 */
public interface Response {
    String getContentType();

    OutputStream getOutputStream();

    PrintWriter getWriter();

    void setCharacterEncoding(String charset);

    void setContentLength(long len);

    void setContentType(String contentType);

    void setHeader(String name, String value);

    String getHeader(String name);
}
