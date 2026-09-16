public Class<?> primaryKeyIdClass(Class<?> entityClass) {
    EntityDescriptor entity = find(entityClass);
    if (entity != null) {
        if (entity.getIdClass() != null) {
            return entity.getIdClass();
        }
        AbstractEntityDescriptor parent = entity.getParent();
        while (parent != null) {
            if (parent.getIdClass() != null) {
                return parent.getIdClass();
            }
            parent = parent.getParent();
        }
    }
    return null;
}