package me.beldon.http;

import ch.qos.logback.core.joran.spi.XMLUtil;

import java.nio.channels.SelectionKey;

/**
 * @author Beldon
 */
public interface Response {

    //服务器名字
    public static final String SERVER_NAME = "demo";

    public String getContentType();

    public int getStatuCode();

    public String getStatuCodeStr();

    public String getHtmlFile();

    public void setHtmlFile(String htmlFile);

    public SelectionKey getKey();

    public void setContentType(String contentType);

    public void setStatuCode(int statuCode);

    public void setStatuCodeStr(String statuCodeStr);
}