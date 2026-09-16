@Override
public boolean methodMatches(Method m) {
    for (String propertyName : propertyNames) {
        if (m.getName().startsWith("get") && Introspector.decapitalize(m.getName().substring(3)).equals(propertyName)) {
            return true;
        }
    }
    return false;
}