package me.beldon.http.context.impl;

import me.beldon.http.context.Request;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Beldon
 */
public class HttpRequest implements Request {

    private static final String SPLIT = "\r\n";
    private static final String CONTENT_TYPE_HEADER = "Content-Type";


    private Map<String, String> headers = new HashMap<>();
    private Map<String, String> parameters = new HashMap<>();
    private Map<String, Object> attribute = new HashMap<>();
    private String method;
    private String uri;
    private String protocol;
    private final byte[] body;
    private final String remoteAddress;

    public HttpRequest(String headerContent, byte[] body, String remoteAddress) {
        this.body = body;
        this.remoteAddress = remoteAddress;
        String[] headers = headerContent.split(SPLIT);
        initMethod(headers[0]);
        initURI(headers[0]);
        initProtocol(headers[0]);
        initRequestHeaders(headers);
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
        return body.length;
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
        return new ByteArrayInputStream(body);
    }

    private void initMethod(String str) {
        method = str.substring(0, str.indexOf(" "));
    }

    private void initURI(String str) {
        uri = str.substring(str.indexOf(" ") + 1, str.indexOf(" ", str.indexOf(" ") + 1));
        if (uri.contains("?")) {
            String attr = uri.substring(uri.indexOf("?") + 1, uri.length());
            uri = uri.substring(0, uri.indexOf("?"));
            initParameter(attr);
        }
    }

    private void initParameter(String attr) {
        String[] attrs = attr.split("&");
        for (String string : attrs) {
            String key = string.substring(0, string.indexOf("="));
            String value = string.substring(string.indexOf("=") + 1);
            parameters.put(key, value);
        }
    }

    private void initProtocol(String str) {
        protocol = str.substring(str.lastIndexOf(" ") + 1, str.length());
    }

    private void initRequestHeaders(String[] strs) {
        for (int i = 1; i < strs.length; i++) {
            String key = strs[i].substring(0, strs[i].indexOf(":"));
            String value = strs[i].substring(strs[i].indexOf(":") + 1);
            headers.put(key, value);
        }
    }
}
