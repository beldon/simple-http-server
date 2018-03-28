package me.beldon.http;

import java.nio.channels.SelectionKey;

/**
 * @author Beldon
 */
public abstract class Context {

    protected Request request;
    protected Response response;

    /**
     * 设置当前连接的上下文
     * @param:  @return
     * @return: Context
     * @Autor: Han
     */
    public abstract void setContext(String requestHeader, SelectionKey key);

    /**
     * 得到Request
     * @param:  @return
     * @return: Request
     * @Autor: Han
     */
    public Request getRequest() {
        return request;
    }

    /**
     * 得到Response
     * @param:  @return
     * @return: Response
     * @Autor: Han
     */
    public Response getResponse() {
        return response;
    }

}
