/**
 * @see DefaultAuthenticationStrategy#decode(String value)
 * Additionally decodes stored login type and domain
 */
@Override
protected String[] decode(String value) {
    if (Strings.isEmpty(value)) {
        return new String[] {};
    }
    String[] values = value.split(VALUE_SEPARATOR);
    String username = getValueAtIndex(values, 0);
    String password = getValueAtIndex(values, 1);
    String type = getValueAtIndex(values, 2);
    String domainId = getValueAtIndex(values, 3);
    return new String[] { username, password, type, domainId };
}
// ---- helper method(s) introduced by the refactoring ----
private String getValueAtIndex(String[] values, int index) {
    return index < values.length ? values[index] : null;
}

