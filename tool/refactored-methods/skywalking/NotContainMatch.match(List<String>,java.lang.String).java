public boolean match(List<String> left, String right) {
    if (Objects.isNull(left)) {
        return false;
    }
    right = trimQuotes(right);
    return !left.contains(right);
}
// ---- helper method(s) introduced by the refactoring ----
private String trimQuotes(String value) {
    if (value.startsWith("\"") && value.endsWith("\"")) {
        return value.substring(1, value.length() - 1);
    }
    return value;
}

