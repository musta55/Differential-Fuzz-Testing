@Override
public T create(Bean<T> bean, CreationalContext<T> creationalContext) {
    Object partialBean = BeanProvider.getContextualReference(this.targetPartialBeanClass);
    return (T) ReflectionUtils.invokeMethod(partialBean, this.producerMethod, Object.class, false);
}