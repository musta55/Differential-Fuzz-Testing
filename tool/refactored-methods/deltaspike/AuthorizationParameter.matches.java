/**
 * TODO comment is no equals!!!
 *
 * @param parameter
 * @return
 */
boolean matches(AuthorizationParameter parameter) {
    if (!type.equals(parameter.type)) {
        return false;
    }
    return matchBindings(parameter);
}
// ---- helper method(s) introduced by the refactoring ----
private void initializeBindings(Set<Annotation> bindings) {
    this.bindings = new HashMap<>();
    for (Annotation bindingAnnotation : bindings) {
        Map<Method, Object> bindingMembers = extractBindingMembers(bindingAnnotation);
        this.bindings.put(bindingAnnotation.annotationType(), bindingMembers);
    }
}

private Map<Method, Object> extractBindingMembers(Annotation bindingAnnotation) {
    Map<Method, Object> bindingMembers = new HashMap<>();
    try {
        for (Method method : bindingAnnotation.annotationType().getDeclaredMethods()) {
            if (method.isAnnotationPresent(Nonbinding.class)) {
                continue;
            }
            bindingMembers.put(method, method.invoke(bindingAnnotation));
        }
    } catch (InvocationTargetException | IllegalAccessException ex) {
        throw new SecurityDefinitionException("Error reading security binding members", ex);
    }
    return bindingMembers;
}

private boolean matchBindings(AuthorizationParameter parameter) {
    for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet()) {
        Map<Method, Object> bindingValues = parameter.bindings.get(bindingEntry.getKey());
        if (bindingValues == null || !matchBindingValues(bindingValues, bindingEntry.getValue())) {
            return false;
        }
    }
    return true;
}

private boolean matchBindingValues(Map<Method, Object> bindingValues, Map<Method, Object> expectedValues) {
    for (Map.Entry<Method, Object> entry : expectedValues.entrySet()) {
        if (!bindingValues.get(entry.getKey()).equals(entry.getValue())) {
            return false;
        }
    }
    return true;
}

private void appendBindings(StringBuilder builder) {
    for (Map.Entry<Class<? extends Annotation>, Map<Method, Object>> bindingEntry : bindings.entrySet()) {
        builder.append('@').append(bindingEntry.getKey().getName()).append('(');
        appendBindingValues(builder, bindingEntry.getValue());
        builder.append(' ');
    }
}

private void appendBindingValues(StringBuilder builder, Map<Method, Object> values) {
    for (Map.Entry<Method, Object> entry : values.entrySet()) {
        builder.append(entry.getKey().getName()).append('=').append(entry.getValue()).append(',');
    }
    if (!values.isEmpty()) {
        builder.setCharAt(builder.length() - 1, ')');
    }
}

private void appendType(StringBuilder builder) {
    if (!bindings.isEmpty()) {
        builder.append(' ');
    }
    builder.append(type);
}

