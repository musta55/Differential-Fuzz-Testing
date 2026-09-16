private Bean<EntityManager> resolveEntityManagerBeans() {
    Set<Bean<?>> entityManagerBeans = beanManager.getBeans(EntityManager.class, new AnyLiteral());
    if (entityManagerBeans == null) {
        entityManagerBeans = new HashSet<Bean<?>>();
    }
    for (Class<? extends Annotation> qualifierClass : qualifiers) {
        for (Bean<?> currentEntityManagerBean : entityManagerBeans) {
            Set<Annotation> foundQualifierAnnotations = currentEntityManagerBean.getQualifiers();
            for (Annotation currentQualifierAnnotation : foundQualifierAnnotations) {
                if (currentQualifierAnnotation.annotationType().equals(qualifierClass)) {
                    return (Bean<EntityManager>) currentEntityManagerBean;
                }
            }
        }
    }
    return null;
}