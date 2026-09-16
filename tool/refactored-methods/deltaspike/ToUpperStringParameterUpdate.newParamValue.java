@Override
public Object newParamValue(Object current) {
    if (isString(current)) {
        return toUpperCase((String) current);
    }
    return current;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isString(Object obj) {
    return obj instanceof String;
}

private String toUpperCase(String str) {
    return str.toUpperCase();
}

