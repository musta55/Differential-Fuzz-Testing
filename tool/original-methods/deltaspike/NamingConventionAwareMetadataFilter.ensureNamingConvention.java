public void ensureNamingConvention(@Observes ProcessAnnotatedType processAnnotatedType) {
    Class<?> beanClass = processAnnotatedType.getAnnotatedType().getJavaClass();
    Named namedAnnotation = beanClass.getAnnotation(Named.class);
    if (namedAnnotation != null && namedAnnotation.value().length() > 0 && Character.isUpperCase(namedAnnotation.value().charAt(0))) {
        AnnotatedTypeBuilder builder = new AnnotatedTypeBuilder();
        builder.readFromType(beanClass);
        String beanName = namedAnnotation.value();
        String newBeanName = beanName.substring(0, 1).toLowerCase() + beanName.substring(1);
        builder.removeFromClass(Named.class).addToClass(new NamedLiteral(newBeanName));
        processAnnotatedType.setAnnotatedType(builder.create());
    }
}