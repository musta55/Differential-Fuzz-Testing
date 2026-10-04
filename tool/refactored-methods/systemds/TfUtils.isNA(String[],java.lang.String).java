/**
 * Function that checks if the given string is one of NA strings.
 *
 * @param NAstrings array of NA strings
 * @param w string to check
 * @return true if w is a NAstring
 */
public static boolean isNA(String[] NAstrings, String w) {
    if (NAstrings == null)
        return false;
    List<String> naList = Arrays.asList(NAstrings);
    return naList.contains(w);
}