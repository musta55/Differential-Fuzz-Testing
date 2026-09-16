/**
 * Remove any non-numeric, non-alphanumeric Characters in the given String
 * @param val
 * @return the original string but any non-numeric, non-alphanumeric is replaced with a '_'
 */
public static String removeSpecialChars(String val) {
    if (val == null) {
        return null;
    }
    int len = val.length();
    char[] newBuf = new char[len];
    val.getChars(0, len, newBuf, 0);
    for (int i = 0; i < len; i++) {
        char c = newBuf[i];
        if (c >= 'a' && c <= 'z' || c >= 'A' && c <= 'Z' || c >= '0' && c <= '9' || c == '-' || c == '_') {
            continue;
        }
        // every other char gets replaced with '_'
        newBuf[i] = '_';
    }
    return new String(newBuf);
}