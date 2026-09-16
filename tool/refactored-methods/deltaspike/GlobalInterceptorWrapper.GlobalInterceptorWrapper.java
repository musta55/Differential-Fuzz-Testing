GlobalInterceptorWrapper(AnnotatedType wrapped, Annotation priorityAnnotation) {
    this.wrapped = wrapped;
    initializeAnnotations(wrapped.getAnnotations(), priorityAnnotation);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeAnnotations(Set<Annotation> originalAnnotationSet, Annotation priorityAnnotation) {
    this.annotations = new HashMap<>(originalAnnotationSet.size());
    populateAnnotationsMap(originalAnnotationSet);
    addPriorityAnnotation(priorityAnnotation);
    createAnnotationSet();
}

private void populateAnnotationsMap(Set<Annotation> originalAnnotationSet) {
    for (Annotation originalAnnotation : originalAnnotationSet) {
        this.annotations.put(originalAnnotation.annotationType(), originalAnnotation);
    }
}

private void addPriorityAnnotation(Annotation priorityAnnotation) {
    this.annotations.put(priorityAnnotation.annotationType(), priorityAnnotation);
}

private void createAnnotationSet() {
    this.annotationSet = new HashSet<>(this.annotations.size());
    this.annotationSet.addAll(this.annotations.values());
}

