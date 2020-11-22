package com.liamnbtech.server.utils.io;

public class RegexUtils {

    public static final String MATCH_IP =
            "(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?).){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)";

    public static String capture(String regex) {
        return "(" + regex + ")";
    }
}
