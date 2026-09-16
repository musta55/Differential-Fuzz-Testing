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
    StringBuilder ret = new StringBuilder(input.length());
    int start = 0;
    int index = -1;
    final int length = pattern.length();
    while ((index = input.indexOf(pattern, start)) >= start) {
        ret.append(input.substring(start, index));
        ret.append(sub);
        start = index + length;
    }
    ret.append(input.substring(start));
    return ret.toString();
}