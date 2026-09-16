public boolean readFrom(AnnotatedElement method, BeanManager beanManager) {
    EntityManagerConfig entityManagerConfig = method.getAnnotation(EntityManagerConfig.class);
    boolean processed = processEntityManagerConfig(beanManager, entityManagerConfig);
    Transactional transactional = method.getAnnotation(Transactional.class);
    processed = processTransactional(processed, transactional);
    return processed;
}