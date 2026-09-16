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
        boolean match = true;
        for (PropertyCriteria c : criteria) {
            if (!c.methodMatches(method)) {
                match = false;
                break;
            }
        }
        if (match) {
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
            boolean match = true;
            for (PropertyCriteria c : criteria) {
                if (!c.fieldMatches(field)) {
                    match = false;
                    break;
                }
            }
            Property<V> prop = Properties.<V>createProperty(field);
            if (match && !resultsContainsProperty(results, prop.getName())) {
                if (!writable || !prop.isReadOnly()) {
                    results.add(prop);
                }
            }
        }
        cls = cls.getSuperclass();
    }
    return results;
}