package me.beldon.http.context;

import java.io.InputStream;

/**
 * @author Beldon
 */
public interface Request {
    Object getAttribute(String name);

    String getCharacterEncoding();

    long getContentLength();

    String getContentType();

    String getMethod();

    String getParameter(String name);

    String getProtocol();

    String getScheme();

    String remoteAddress();

    void setAttribute(String name, Object obj);

    void removeAttribute(String name);

    InputStream getInputStream();
}
