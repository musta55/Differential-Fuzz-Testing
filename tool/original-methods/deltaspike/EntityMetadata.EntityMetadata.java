public EntityMetadata(Class<?> entityClass, String entityName, Class<? extends Serializable> primaryKeyClass) {
    this.entityClass = entityClass;
    this.entityName = entityName;
    this.primaryKeyClass = primaryKeyClass;
}