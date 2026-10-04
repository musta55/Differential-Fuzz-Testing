/**
 * Determine if the HTTP status code is successful or not i.e. in range 200
 * to 399 inclusive
 *
 * @param codeAsString
 *            status code to check
 * @return whether in range 200-399 or not
 */
public static boolean isSuccessCode(String codeAsString) {
    if (StringUtils.isNumeric(codeAsString)) {
        try {
            int code = Integer.parseInt(codeAsString);
            return isSuccessCode(code);
        } catch (NumberFormatException ex) {
            return false;
        }
    }
    return false;
}