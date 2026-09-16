protected AnnotatedImpl(Class<?> type, AnnotationStore annotations, Type genericType, Type overriddenType) {
    this.type = initializeType(type, genericType, overriddenType);
    this.typeClosure = initializeTypeClosure(type, genericType, overriddenType);
    this.annotations = initializeAnnotations(annotations);
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

