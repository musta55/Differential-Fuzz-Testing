public String[] primaryKeyFields(Class<?> entityClass) {
    EntityDescriptor entity = find(entityClass);
    if (entity != null) {
        if (entity.getId() != null) {
            return entity.getId();
        }
        AbstractEntityDescriptor parent = entity.getParent();
        while (parent != null) {
            if (parent.getId() != null) {
                return parent.getId();
            }
            parent = parent.getParent();
        }
    }
    return null;
}