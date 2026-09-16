/**
 * {@inheritDoc}
 */
@Override
public Set<Type> getTypeClosure() {
    return new HashSet<>(typeClosure);
}
// ---- helper method(s) introduced by the refactoring ----
private Type initializeType(Class<?> type, Type genericType, Type overriddenType) {
    if (overriddenType != null) {
        return overriddenType;
    }
    return genericType != null ? genericType : type;
}

private Set<Type> initializeTypeClosure(Class<?> type, Type genericType, Type overriddenType) {
    if (overriddenType != null) {
        return Collections.singleton(overriddenType);
    }
    return new HierarchyDiscovery(genericType != null ? genericType : type).getTypeClosure();
}

private AnnotationStore initializeAnnotations(AnnotationStore annotations) {
    return annotations != null ? annotations : new AnnotationStore();
}

