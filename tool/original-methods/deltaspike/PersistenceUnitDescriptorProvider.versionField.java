public String versionField(Class<?> entityClass) {
    EntityDescriptor entity = find(entityClass);
    if (entity != null) {
        if (!StringUtils.isEmpty(entity.getVersion())) {
            return entity.getVersion();
        }
        AbstractEntityDescriptor parent = entity.getParent();
        while (parent != null) {
            if (!StringUtils.isEmpty(parent.getVersion())) {
                return parent.getVersion();
            }
            parent = parent.getParent();
        }
    }
    return null;
}