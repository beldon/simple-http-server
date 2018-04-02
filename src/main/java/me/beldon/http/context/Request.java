package me.beldon.http.context;

/**
 * @author Beldon
 */
public interface Request {
    Object getAttribute(String name);

    String getCharacterEncoding();

    long getContentLength();

    String getContentType();


    String getParameter();


    String getProtocol();

    String getScheme();

    String remoteAddress();

    void setAttribute(String name, Object obj);

    void removeAttribute(String name);
}
