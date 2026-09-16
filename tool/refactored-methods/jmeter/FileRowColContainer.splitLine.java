/**
 * Splits the line according to the specified delimiter
 *
 * @return a List of Strings containing one element for each value in
 *         the line
 */
private static List<String> splitLine(String theLine, String delim) {
    List<String> result = new ArrayList<>();
    String[] tokens = theLine.split(Pattern.quote(delim), -1);
    for (String token : tokens) {
        result.add(token);
    }
    return result;
}