private EntityMetadata extractFromType(Type type) {
    LOG.log(Level.FINER, "extractFrom: type = {0}", type);
    if (!(type instanceof ParameterizedType)) {
        return null;
    }
    ParameterizedType parametrizedType = (ParameterizedType) type;
    Type[] genericTypes = parametrizedType.getActualTypeArguments();
    EntityMetadata result = null;
    // don't use a foreach here, we must be sure that the we first get the entity type
    for (Type genericType : genericTypes) {
        if (genericType instanceof Class && EntityUtils.isEntityClass((Class<?>) genericType)) {
            result = new EntityMetadata((Class<?>) genericType);
            continue;
        }
        if (result != null && genericType instanceof Class) {
            result.setPrimaryKeyClass((Class<? extends Serializable>) genericType);
            return result;
        }
    }
    return result;
}