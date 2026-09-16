@Override
public Object getIdentifier(Object entity) {
    final String methodName = "getIdObject";
    try {
        if (!entityManager.contains(entity)) {
            entity = entityManager.getReference(entity.getClass(), EntityUtils.primaryKeyValue(entity));
        }
        final Object identifier = persistenceUnitUtil.getIdentifier(entity);
        if (identifier != null) {
            final Method method;
            method = identifier.getClass().getMethod(methodName);
            return method.invoke(identifier);
        }
    } catch (NoSuchMethodException e) {
        throw new RuntimeException(e);
    } catch (InvocationTargetException e) {
        throw new RuntimeException(e);
    } catch (IllegalAccessException e) {
        throw new RuntimeException(e);
    } catch (IllegalStateException e) {
        return null;
    }
    return null;
}