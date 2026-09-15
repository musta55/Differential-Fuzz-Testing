package org.apache.deltaspike.core.util;

import javax.enterprise.inject.Typed;

@Typed()
public abstract class StringUtils
{
    /**
     * Constructor which prevents the instantiation of this class
     */
    private StringUtils()
    {
        // prevent instantiation
    }

    public static boolean isEmpty(String string)
    {
        return string == null || string.trim().isEmpty();
    }

    public static boolean isNotEmpty(String text)
    {
        return !isEmpty(text);
    }

    /**
     * Remove any non-numeric, non-alphanumeric Characters in the given String
     * @param val
     * @return the original string but any non-numeric, non-alphanumeric is replaced with a '_'
     */
    public static String removeSpecialChars(String val)
    {
        if (val == null)
        {
            return null;
        }
        return val.replaceAll("[^a-zA-Z0-9-_]", "_");
    }
}