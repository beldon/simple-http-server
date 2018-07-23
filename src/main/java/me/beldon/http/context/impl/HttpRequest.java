package me.beldon.http.context.impl;

import lombok.extern.slf4j.Slf4j;
import me.beldon.http.context.Request;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Beldon
 */
@Slf4j
public class HttpRequest implements Request {

    private static final String CONTENT_TYPE_HEADER = "Content-Type";


    private Map<String, String> headers;
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, Object> attribute = new HashMap<>();
    private String method;
    private String uri;
    private String protocol;
    private String remoteAddress;
    private InputStream body;


    public HttpRequest(Map<String, String> headers, String remoteAddress, String uri, String protocol, String method, InputStream inputStream) {
        this.headers = headers;
        this.remoteAddress = remoteAddress;
        this.uri = uri;
        this.protocol = protocol;
        this.method = method;
        this.body = inputStream;
    }

    @Override
    public Object getAttribute(String name) {
        return attribute.get(name);
    }

    @Override
    public String getCharacterEncoding() {
        return null;
    }

    @Override
    public long getContentLength() {
        try {
            return body.available();
        } catch (IOException e) {
            log.error("get body available error.", e);
        }
        return 0;
    }

    @Override
    public String getContentType() {
        return headers.get(CONTENT_TYPE_HEADER);
    }

    @Override
    public String getMethod() {
        return method;
    }

    @Override
    public String getParameter(String name) {
        return parameters.get(name);
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getScheme() {
        return null;
    }

    @Override
    public String remoteAddress() {
        return remoteAddress;
    }

    @Override
    public void setAttribute(String name, Object obj) {
        attribute.put(name, obj);
    }

    @Override
    public void removeAttribute(String name) {
        attribute.remove(name);
    }

    @Override
    public InputStream getInputStream() {
        return body;
    }

    @Override
    public String getUri() {
        return uri;
    }
}
