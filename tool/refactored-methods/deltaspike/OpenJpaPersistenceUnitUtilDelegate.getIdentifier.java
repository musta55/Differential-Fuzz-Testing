@Override
public Object getIdentifier(Object entity) {
    final String methodName = "getIdObject";
    try {
        if (!entityManager.contains(entity)) {
            entity = entityManager.getReference(entity.getClass(), EntityUtils.primaryKeyValue(entity));
        }
        Object identifier = persistenceUnitUtil.getIdentifier(entity);
        if (identifier != null) {
            Method method = identifier.getClass().getMethod(methodName);
            return method.invoke(identifier);
        }
    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
        throw new RuntimeException(e);
    } catch (IllegalStateException e) {
        return null;
    }
    return null;
}