package me.beldon.http;

import java.nio.channels.SelectionKey;

public class HttpResponse implements Response {

    private SelectionKey key;
    //内容类型  defalut 为text/html
    private String contentType = "text/html";
    //响应码  defalut 为200
    private int StatuCode = 200;
    private String statuCodeStr = "OK";
    private String htmlFile = "";

    public HttpResponse(SelectionKey key) {
        this.key = key;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public int getStatuCode() {
        return StatuCode;
    }

    @Override
    public SelectionKey getKey() {
        return key;
    }

    @Override
    public String getStatuCodeStr() {
        return statuCodeStr;
    }

    @Override
    public String getHtmlFile() {
        return htmlFile;
    }

    @Override
    public void setHtmlFile(String htmlFile) {
        this.htmlFile = htmlFile;
    }

    @Override
    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    @Override
    public void setStatuCode(int statuCode) {
        StatuCode = statuCode;
    }

    @Override
    public void setStatuCodeStr(String statuCodeStr) {
        this.statuCodeStr = statuCodeStr;
    }
}