private Bean<EntityManager> resolveEntityManagerBeans() {
    Set<Bean<?>> entityManagerBeans = beanManager.getBeans(EntityManager.class, new AnyLiteral());
    if (entityManagerBeans.isEmpty()) {
        return null;
    }
    for (Bean<?> bean : entityManagerBeans) {
        for (Class<? extends Annotation> qualifier : qualifiers) {
            if (bean.getQualifiers().stream().anyMatch(q -> q.annotationType().equals(qualifier))) {
                return (Bean<EntityManager>) bean;
            }
        }
    }
    return null;
}
// ---- helper method(s) introduced by the refactoring ----
private String buildErrorMessage() {
    StringBuilder qualifierNames = new StringBuilder();
    for (Class<?> c : qualifiers) {
        qualifierNames.append(c.getName()).append(" ");
    }
    return "Cannot find an EntityManager qualified with [" + qualifierNames + "]. Did you add a corresponding producer?";
}

