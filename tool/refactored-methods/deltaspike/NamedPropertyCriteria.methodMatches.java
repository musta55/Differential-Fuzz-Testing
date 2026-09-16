@Override
public boolean methodMatches(Method m) {
    for (String propertyName : propertyNames) {
        if (isGetterMethod(m, propertyName)) {
            return true;
        }
    }
    return false;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isGetterMethod(Method m, String propertyName) {
    return m.getName().startsWith("get") && Introspector.decapitalize(m.getName().substring(3)).equals(propertyName);
}

