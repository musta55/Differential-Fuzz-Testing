public boolean readFrom(AnnotatedElement method, BeanManager beanManager) {
    EntityManagerConfig entityManagerConfig = method.getAnnotation(EntityManagerConfig.class);
    boolean processed = handleEntityManagerConfig(beanManager, entityManagerConfig);
    Transactional transactional = method.getAnnotation(Transactional.class);
    processed = handleTransactional(transactional, processed);
    return processed;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean handleTransactional(Transactional transactional, boolean processed) {
    if (transactional != null) {
        processed = updateQualifiersFromTransactional(transactional, processed);
        this.readOnly = transactional.readOnly();
    }
    return processed;
}

private boolean updateQualifiersFromTransactional(Transactional transactional, boolean processed) {
    if (this.qualifiers == null) {
        processed = true;
        this.setQualifiers(transactional.qualifier());
    }
    return processed;
}

private boolean handleEntityManagerConfig(BeanManager beanManager, EntityManagerConfig entityManagerConfig) {
    boolean processed = false;
    if (entityManagerConfig != null) {
        processed = true;
        updateEntityManagerConfigFields(entityManagerConfig);
        updateEntityManagerResolver(beanManager, entityManagerConfig);
    }
    return processed;
}

private void updateEntityManagerConfigFields(EntityManagerConfig entityManagerConfig) {
    this.setEntityManagerFlushMode(entityManagerConfig.flushMode());
    this.setQualifiers(entityManagerConfig.qualifier());
}

private void updateEntityManagerResolver(BeanManager beanManager, EntityManagerConfig entityManagerConfig) {
    Class<? extends EntityManagerResolver> resolverClass = entityManagerConfig.entityManagerResolver();
    if (!resolverClass.equals(EntityManagerResolver.class)) {
        this.setEntityManagerResolverClass(resolverClass);
        determineResolverScope(beanManager, resolverClass);
    } else {
        this.setEntityManagerResolverIsNormalScope(false);
    }
}

private void determineResolverScope(BeanManager beanManager, Class<? extends EntityManagerResolver> resolverClass) {
    Set<Bean<?>> beans = beanManager.getBeans(resolverClass);
    Class<? extends Annotation> scope = beanManager.resolve(beans).getScope();
    this.setEntityManagerResolverIsNormalScope(beanManager.isNormalScope(scope));
}

