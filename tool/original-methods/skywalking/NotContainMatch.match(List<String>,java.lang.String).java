public boolean match(List<String> left, String right) {
    if (Objects.isNull(left)) {
        return false;
    }
    if (right.startsWith("\"") && right.endsWith("\"")) {
        right = right.substring(1, right.length() - 1);
    }
    return !left.contains(right);
}