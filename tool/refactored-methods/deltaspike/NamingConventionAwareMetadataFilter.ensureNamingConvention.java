public void ensureNamingConvention(@Observes ProcessAnnotatedType processAnnotatedType) {
    Class<?> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();
    Named namedAnnotation = beanClass.getAnnotation(Named.class);
    if (namedAnnotation != null && shouldModifyBeanName(namedAnnotation.value())) {
        AnnotatedTypeBuilder builder = new AnnotatedTypeBuilder();
        builder.readFromType(beanClass);
        String newBeanName = modifyBeanName(namedAnnotation.value());
        builder.removeFromClass(Named.class).addToClass(new NamedLiteral(newBeanName));
        processAnnotatedType.setAnnotatedType(builder.create());
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldModifyBeanName(String beanName) {
    return beanName.length() > 0 && Character.isUpperCase(beanName.charAt(0));
}

private String modifyBeanName(String beanName) {
    return beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
}

