public PartialBeanDescriptor(Class<? extends Annotation> binding, Class<? extends InvocationHandler> handler, Class<?> clazz) {
    this(binding, handler);
    this.classes.add(clazz);
}