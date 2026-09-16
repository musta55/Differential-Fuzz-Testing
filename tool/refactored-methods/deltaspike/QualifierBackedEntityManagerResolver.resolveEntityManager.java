@Override
public EntityManager resolveEntityManager() {
    Bean<EntityManager> entityManagerBean = resolveEntityManagerBeans();
    if (entityManagerBean == null) {
        throw new IllegalStateException(buildErrorMessage());
    }
    return (EntityManager) beanManager.getReference(entityManagerBean, EntityManager.class, beanManager.createCreationalContext(entityManagerBean));
}
// ---- helper method(s) introduced by the refactoring ----
private String buildErrorMessage() {
    StringBuilder qualifierNames = new StringBuilder();
    for (Class<?> c : qualifiers) {
        qualifierNames.append(c.getName()).append(" ");
    }
    return "Cannot find an EntityManager qualified with [" + qualifierNames + "]. Did you add a corresponding producer?";
}

