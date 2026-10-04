/*
     * @param element
     */
@Override
public void modifyTestElement(TestElement element) {
    configureTestElement(element);
    String md5HexString = normalizeMd5HexString(this.md5HexInput.getText());
    ((MD5HexAssertion) element).setAllowedMD5Hex(md5HexString);
}
// ---- helper method(s) introduced by the refactoring ----
private static String normalizeMd5HexString(String md5HexString) {
    if (isEmpty(md5HexString)) {
        return "";
    }
    return md5HexString;
}

private static boolean isEmpty(String str) {
    return str == null || str.isEmpty();
}

