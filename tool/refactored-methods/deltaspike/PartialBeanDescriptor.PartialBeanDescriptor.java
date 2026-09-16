public PartialBeanDescriptor(Class<? extends Annotation> binding, Class<? extends InvocationHandler> handler, Class<?> clazz) {
    this.binding = binding;
    this.handler = handler;
    this.classes = new HashSet<>();
    if (clazz != null) {
        this.classes.add(clazz);
    }
}