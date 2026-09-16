/**
 * Remove any non-numeric, non-alphanumeric Characters in the given String
 * @param val
 * @return the original string but any non-numeric, non-alphanumeric is replaced with a '_'
 */
public static String removeSpecialChars(String val) {
    if (val == null) {
        return null;
    }
    return val.replaceAll("[^a-zA-Z0-9-_]", "_");
}