package me.beldon.http.util;

public class StringUtils {
    public static final boolean hasText(String str) {
        return str != null && str.trim().length() > 0;
    }
}
