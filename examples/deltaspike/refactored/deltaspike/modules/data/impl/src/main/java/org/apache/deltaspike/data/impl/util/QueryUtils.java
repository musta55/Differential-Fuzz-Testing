package org.apache.deltaspike.data.impl.util;

import java.text.MessageFormat;
import org.apache.deltaspike.core.util.StringUtils;

public final class QueryUtils
{
    private static final String KEYWORD_SPLITTER = "({0})(?=[A-Z])";

    private QueryUtils()
    {
    }

    public static String[] splitByKeyword(String query, String keyword)
    {
        return query.split(MessageFormat.format(KEYWORD_SPLITTER, keyword));
    }

    public static String uncapitalize(String value)
    {
        if (StringUtils.isEmpty(value))
        {
            return null;
        }
        if (value.length() == 1)
        {
            return value.toLowerCase();
        }
        return value.substring(0, 1).toLowerCase() + value.substring(1);
    }

    public static boolean isString(Object value)
    {
        return value != null && value instanceof String;
    }

    public static String nullSafeValue(String value)
    {
        return nullSafeValue(value, null);
    }

    public static String nullSafeValue(String value, String fallback)
    {
        if (value != null)
        {
            return value;
        }
        return fallback != null ? fallback : "";
    }
}