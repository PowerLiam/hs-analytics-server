package com.liamnbtech.server.utils.io;

import java.util.List;

public class UrlUtils {
    private static final String QUERY_STRING_DELIMITER = "&";
    private static final String QUERY_STRING_PATTERN = "%s=%s";

    public static String toQueryParamString(String name, String value) {
        return String.format(QUERY_STRING_PATTERN, name, value);
    }

    public static String toQueryString(List<String> queryParamStrings) {
        return String.join(UrlUtils.QUERY_STRING_DELIMITER, queryParamStrings);
    }

}
