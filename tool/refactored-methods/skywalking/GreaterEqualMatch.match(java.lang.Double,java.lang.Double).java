public boolean match(Double left, Double right) {
    return compare(left, right);
}
// ---- helper method(s) introduced by the refactoring ----
private <T extends Comparable<T>> boolean compare(T left, T right) {
    return left != null && right != null && left.compareTo(right) >= 0;
}

