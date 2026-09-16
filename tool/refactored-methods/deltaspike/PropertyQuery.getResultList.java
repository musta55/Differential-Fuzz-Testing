/**
 * Get the result from the query, causing the query to be run.
 *
 * @param writable
 *            if this query should only return properties that are not read only
 * @return the results, or an empty list if there are no results
 */
private List<Property<V>> getResultList(boolean writable) {
    List<Property<V>> results = new ArrayList<Property<V>>();
    // First check public accessor methods (we ignore private methods)
    for (Method method : targetClass.getMethods()) {
        if (!(method.getName().startsWith("is") || method.getName().startsWith("get"))) {
            continue;
        }
        if (matchesCriteria(method)) {
            MethodProperty<V> property = Properties.<V>createProperty(method);
            if (!writable || !property.isReadOnly()) {
                results.add(property);
            }
        }
    }
    Class<?> cls = targetClass;
    while (cls != null && !cls.equals(Object.class)) {
        // Now check declared fields
        for (Field field : cls.getDeclaredFields()) {
            if (matchesCriteria(field) && !resultsContainsProperty(results, field.getName())) {
                Property<V> prop = Properties.<V>createProperty(field);
                if (!writable || !prop.isReadOnly()) {
                    results.add(prop);
                }
            }
        }
        cls = cls.getSuperclass();
    }
    return results;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean matchesCriteria(Method method) {
    for (PropertyCriteria c : criteria) {
        if (!c.methodMatches(method)) {
            return false;
        }
    }
    return true;
}

private boolean matchesCriteria(Field field) {
    for (PropertyCriteria c : criteria) {
        if (!c.fieldMatches(field)) {
            return false;
        }
    }
    return true;
}

