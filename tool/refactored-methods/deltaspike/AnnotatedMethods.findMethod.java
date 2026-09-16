public static AnnotatedMethod<?> findMethod(final AnnotatedType<?> type, final Method method) {
    return type.getMethods().stream().filter(am -> am.getJavaMember().equals(method)).findFirst().orElseThrow(() -> new IllegalStateException("No annotated method for " + method));
}