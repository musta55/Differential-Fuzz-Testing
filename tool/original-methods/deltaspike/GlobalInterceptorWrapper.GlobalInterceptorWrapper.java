GlobalInterceptorWrapper(AnnotatedType wrapped, Annotation priorityAnnotation) {
    this.wrapped = wrapped;
    Set<Annotation> originalAnnotationSet = wrapped.getAnnotations();
    this.annotations = new HashMap<Class<? extends Annotation>, Annotation>(originalAnnotationSet.size());
    for (Annotation originalAnnotation : originalAnnotationSet) {
        this.annotations.put(originalAnnotation.annotationType(), originalAnnotation);
    }
    this.annotations.put(priorityAnnotation.annotationType(), priorityAnnotation);
    this.annotationSet = new HashSet<Annotation>(this.annotations.size());
    this.annotationSet.addAll(this.annotations.values());
}