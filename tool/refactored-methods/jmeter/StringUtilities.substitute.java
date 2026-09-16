/**
 * Replace all patterns in a String
 *
 * @see String#replaceAll(String,String)
 *  - JDK1.4 only
 *
 * @param input - string to be transformed
 * @param pattern - pattern to replace
 * @param sub - replacement
 * @return the updated string
 */
public static String substitute(final String input, final String pattern, final String sub) {
    if (pattern.isEmpty()) {
        return input;
    }
    StringBuilder result = new StringBuilder(input.length());
    int start = 0;
    int index;
    while ((index = input.indexOf(pattern, start)) != -1) {
        result.append(input, start, index).append(sub);
        start = index + pattern.length();
    }
    result.append(input.substring(start));
    return result.toString();
}