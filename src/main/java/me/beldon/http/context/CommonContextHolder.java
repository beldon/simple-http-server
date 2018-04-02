package me.beldon.http.context;

/**
 * @author Beldon
 * @create 2018-04-02 下午6:21
 */
public class CommonContextHolder {


    public static class RequestHolder {
        private static ThreadLocal<Request> requestThreadLocal = new ThreadLocal<>();

        public static void set(Request request) {
            requestThreadLocal.set(request);
        }

        public static Request get() {
            return requestThreadLocal.get();
        }

        public static void remove() {
            requestThreadLocal.remove();
        }
    }
}

