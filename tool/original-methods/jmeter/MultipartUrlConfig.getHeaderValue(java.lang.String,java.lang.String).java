private static String getHeaderValue(String headerName, String multiPart) {
    //$NON-NLS-1$
    String regularExpression = headerName + "\\s*:\\s*(.*)$";
    if (USE_JAVA_REGEX) {
        return getHeaderValueWithJavaRegex(multiPart, regularExpression);
    }
    return getHeaderValueWithOroRegex(multiPart, regularExpression);
}