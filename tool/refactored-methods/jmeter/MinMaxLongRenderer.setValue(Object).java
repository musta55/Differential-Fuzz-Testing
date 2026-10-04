@Override
public void setValue(Object value) {
    if (value instanceof Long && !isExtremum((Long) value)) {
        setText(formatter.format((Long) value));
        return;
    }
    setText("#N/A");
}
// ---- helper method(s) introduced by the refactoring ----
private static boolean isExtremum(Long value) {
    return value == Long.MAX_VALUE || value == Long.MIN_VALUE;
}

