@Override
protected Class<?>[] getAdditionalInterfacesToImplement(Class<?> targetClass) {
    return Arrays.stream(targetClass.getInterfaces()).filter(itf -> itf != PartialStateHolder.class).toArray(Class[]::new);
}