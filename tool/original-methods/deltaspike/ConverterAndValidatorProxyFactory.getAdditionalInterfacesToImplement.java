@Override
protected Class<?>[] getAdditionalInterfacesToImplement(Class<?> targetClass) {
    List<Class<?>> interfaces = Arrays.asList(targetClass.getInterfaces());
    if (!interfaces.contains(PartialStateHolder.class)) {
        return new Class<?>[] { PartialStateHolder.class };
    }
    return null;
}