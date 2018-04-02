package me.beldon.http.context.impl;

import me.beldon.http.context.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Beldon
 */
public class HttpRequest implements Request {

    private Map<String, Object> attrs = new HashMap<>();

    private Map<String, Object> parameters = new HashMap<>();

    @Override
    public Object getAttribute(String name) {
        return null;
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public long getContentLength() {
        return 0;
    }

    @Override
    public String getContentType() {
        return null;
    }

    @Override
    public String getParameter() {
        return null;
    }

    @Override
    public String getProtocol() {
        return null;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String remoteAddress() {
        return null;
    }

    @Override
    public void setAttribute(String name, Object obj) {

    }

    @Override
    public void removeAttribute(String name) {

    }
}
