@Override
public String getPropertyValue(String key) {
    String val = super.getPropertyValue(key);
    if (val == null || val.isEmpty()) {
        val = super.getPropertyValue(normalizeKey(key));
    }
    return val;
}
// ---- helper method(s) introduced by the refactoring ----
private String normalizeKey(String key) {
    return key.replace('.', '_');
}

