package me.beldon.http.context;

import java.io.PrintWriter;

/**
 * @author Beldon
 */
public interface Response {
    String getContentType();

    void getOutputStream();

    PrintWriter getWriter();

    void setCharacterEncoding(String charset);

    void setContentLength(long len);

    void setContentType(String type);
}
