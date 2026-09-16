public static AnnotatedMethod<?> findMethod(final AnnotatedType<?> type, final Method method) {
    AnnotatedMethod<?> annotatedMethod = null;
    for (final AnnotatedMethod<?> am : type.getMethods()) {
        if (am.getJavaMember().equals(method)) {
            annotatedMethod = am;
            break;
        }
    }
    if (annotatedMethod == null) {
        throw new IllegalStateException("No annotated method for " + method);
    }
    return annotatedMethod;
}