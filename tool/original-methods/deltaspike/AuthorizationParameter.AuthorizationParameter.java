AuthorizationParameter(Type type, Set<Annotation> bindings) {
    this.type = type;
    this.bindings = new HashMap<Class<? extends Annotation>, Map<Method, Object>>();
    for (Annotation bindingAnnotation : bindings) {
        Map<Method, Object> bindingMembers = new HashMap<Method, Object>();
        try {
            for (Method method : bindingAnnotation.annotationType().getDeclaredMethods()) {
                if (method.isAnnotationPresent(Nonbinding.class)) {
                    continue;
                }
                bindingMembers.put(method, method.invoke(bindingAnnotation));
            }
        } catch (InvocationTargetException ex) {
            throw new SecurityDefinitionException("Error reading security binding members", ex);
        } catch (IllegalAccessException ex) {
            throw new SecurityDefinitionException("Error reading security binding members", ex);
        }
        this.bindings.put(bindingAnnotation.annotationType(), bindingMembers);
    }
}