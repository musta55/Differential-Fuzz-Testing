/**
 * Splits the line according to the specified delimiter
 *
 * @return a List of Strings containing one element for each value in
 *         the line
 */
private static List<String> splitLine(String theLine, String delim) {
    List<String> result = new ArrayList<>();
    StringTokenizer tokener = new StringTokenizer(theLine, delim, true);
    /*
         * the beginning of the line is a "delimiter" so that ,a,b,c returns ""
         * "a" "b" "c"
         */
    boolean lastWasDelim = true;
    while (tokener.hasMoreTokens()) {
        String token = tokener.nextToken();
        if (token.equals(delim)) {
            if (lastWasDelim) {
                // two delimiters in a row; add an empty String
                result.add("");
            }
            lastWasDelim = true;
        } else {
            lastWasDelim = false;
            result.add(token);
        }
    }
    if (// Catch the trailing delimiter
    lastWasDelim) {
        // $NON-NLS-1$
        result.add("");
    }
    return result;
}