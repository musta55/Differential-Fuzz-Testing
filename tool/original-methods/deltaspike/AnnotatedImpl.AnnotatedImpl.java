protected AnnotatedImpl(Class<?> type, AnnotationStore annotations, Type genericType, Type overriddenType) {
    if (overriddenType == null) {
        if (genericType != null) {
            typeClosure = new HierarchyDiscovery(genericType).getTypeClosure();
            this.type = genericType;
        } else {
            typeClosure = new HierarchyDiscovery(type).getTypeClosure();
            this.type = type;
        }
    } else {
        this.type = overriddenType;
        typeClosure = Collections.singleton(overriddenType);
    }
    if (annotations == null) {
        this.annotations = new AnnotationStore();
    } else {
        this.annotations = annotations;
    }
}