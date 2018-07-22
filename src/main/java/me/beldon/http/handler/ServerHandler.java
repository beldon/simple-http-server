package me.beldon.http.handler;

import me.beldon.http.context.Request;
import me.beldon.http.context.Response;

public interface ServerHandler {
    void handler(Request request, Response response) throws Exception;
}
