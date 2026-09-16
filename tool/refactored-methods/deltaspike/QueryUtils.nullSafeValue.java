public static String nullSafeValue(String value, String fallback) {
    if (value != null) {
        return value;
    }
    return fallback != null ? fallback : "";
}