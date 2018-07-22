package me.beldon.http.context.impl;

import me.beldon.http.context.Response;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

public class DefaultHttpResponse implements Response {

    private final OutputStream outputStream;

    private String contentType;

    private String charset;

    private long contentLength;

    private Map<String, String> headers = new HashMap<>();


    public DefaultHttpResponse(OutputStream outputStream) {
        this.outputStream = outputStream;
    }


    @Override
    public String getContentType() {
        return this.contentType;
    }

    @Override
    public OutputStream getOutputStream() {
        return outputStream;
    }

    @Override
    public PrintWriter getWriter() {
        return new PrintWriter(outputStream);
    }

    @Override
    public void setCharacterEncoding(String charset) {
        this.charset = charset;
    }

    @Override
    public void setContentLength(long len) {
        this.contentLength = len;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setHeader(String name, String value) {
        headers.put(name, value);
    }

    @Override
    public String getHeader(String name) {
        return headers.get(name);
    }
}
