package me.beldon.http.handler;

import java.io.IOException;

/**
 * @author Beldon
 * @create 2018-04-02 下午6:33
 */
public interface Handler<T, V> {
    V handle(T t) throws IOException;
}
