public static String nullSafeValue(String value, String fallback) {
    return value != null ? value : (fallback != null ? fallback : "");
}