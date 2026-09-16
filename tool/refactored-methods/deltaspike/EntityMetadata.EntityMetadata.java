public EntityMetadata(Class<?> entityClass, String entityName, Class<? extends Serializable> primaryKeyClass) {
    this(entityClass, entityName, primaryKeyClass, null, null);
}
// ---- helper method(s) introduced by the refactoring ----
public EntityMetadata(Class<?> entityClass, String entityName, Class<? extends Serializable> primaryKeyClass, Property<Serializable> primaryKeyProperty, Property<Serializable> versionProperty) {
    this.entityClass = entityClass;
    this.entityName = entityName;
    this.primaryKeyClass = primaryKeyClass;
    this.primaryKeyProperty = primaryKeyProperty;
    this.versionProperty = versionProperty;
}

