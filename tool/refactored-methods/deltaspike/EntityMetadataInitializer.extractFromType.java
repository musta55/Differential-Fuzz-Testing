private EntityMetadata extractFromType(Type type) {
    LOG.log(Level.FINER, "extractFrom: type = {0}", type);
    if (!(type instanceof ParameterizedType)) {
        return null;
    }
    ParameterizedType parameterizedType = (ParameterizedType) type;
    Type[] genericTypes = parameterizedType.getActualTypeArguments();
    EntityMetadata result = findEntityType(genericTypes);
    if (result != null) {
        setPrimaryKeyClass(result, genericTypes);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private EntityMetadata findEntityType(Type[] genericTypes) {
    for (Type genericType : genericTypes) {
        if (genericType instanceof Class && EntityUtils.isEntityClass((Class<?>) genericType)) {
            return new EntityMetadata((Class<?>) genericType);
        }
    }
    return null;
}

private void setPrimaryKeyClass(EntityMetadata result, Type[] genericTypes) {
    for (Type genericType : genericTypes) {
        if (genericType instanceof Class) {
            result.setPrimaryKeyClass((Class<? extends Serializable>) genericType);
            return;
        }
    }
}

