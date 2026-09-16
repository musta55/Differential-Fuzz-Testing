@Override
public T create(Bean<T> bean, CreationalContext<T> creationalContext) {
    Object partialBean = BeanProvider.getContextualReference(targetPartialBeanClass);
    return (T) ReflectionUtils.invokeMethod(partialBean, producerMethod, Object.class, false);
}