@Override
public EntityManager resolveEntityManager() {
    Bean<EntityManager> entityManagerBean = resolveEntityManagerBeans();
    if (entityManagerBean == null) {
        StringBuilder qualifierNames = new StringBuilder();
        for (Class<?> c : qualifiers) {
            qualifierNames.append(c.getName()).append(" ");
        }
        throw new IllegalStateException("Cannot find an EntityManager qualified with [" + qualifierNames + "]. Did you add a corresponding producer?");
    }
    return (EntityManager) beanManager.getReference(entityManagerBean, EntityManager.class, beanManager.createCreationalContext(entityManagerBean));
}