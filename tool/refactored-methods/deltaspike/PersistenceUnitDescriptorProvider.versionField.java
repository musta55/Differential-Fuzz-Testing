public String versionField(Class<?> entityClass) {
    return findInHierarchy(entityClass, entity -> StringUtils.isEmpty(entity.getVersion()) ? null : entity.getVersion());
}
// ---- helper method(s) introduced by the refactoring ----
private <T> T findInHierarchy(Class<?> entityClass, java.util.function.Function<AbstractEntityDescriptor, T> extractor) {
    EntityDescriptor entity = find(entityClass);
    if (entity != null) {
        T result = extractor.apply(entity);
        if (result != null) {
            return result;
        }
        AbstractEntityDescriptor parent = entity.getParent();
        while (parent != null) {
            result = extractor.apply(parent);
            if (result != null) {
                return result;
            }
            parent = parent.getParent();
        }
    }
    return null;
}

