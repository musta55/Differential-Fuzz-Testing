/**
 * Determine if the HTTP status code is successful or not i.e. in range 200
 * to 399 inclusive
 *
 * @param codeAsString
 *            status code to check
 * @return whether in range 200-399 or not
 */
public static boolean isSuccessCode(String codeAsString) {
    if (!StringUtils.isNumeric(codeAsString)) {
        return false;
    }
    try {
        int code = parseCode(codeAsString);
        return isSuccessCode(code);
    } catch (NumberFormatException ex) {
        return false;
    }
}
// ---- helper method(s) introduced by the refactoring ----
/**
 * Parses the string representation of a code to an integer.
 *
 * @param codeAsString the string representation of the code
 * @return the parsed integer code
 * @throws NumberFormatException if the string cannot be parsed as an integer
 */
private static int parseCode(String codeAsString) {
    return Integer.parseInt(codeAsString);
}

