public MethodPropertyImpl(Method method) {
    validateMethod(method);
    String propertyNameInAccessorMethod = extractPropertyName(method);
    this.propertyName = Introspector.decapitalize(propertyNameInAccessorMethod);
    this.getterMethod = getGetterMethod(method.getDeclaringClass(), propertyName);
    this.setterMethod = getSetterMethod(method.getDeclaringClass(), propertyName);
}
// ---- helper method(s) introduced by the refactoring ----
private void validateMethod(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith(GETTER_METHOD_PREFIX)) {
        validateGetterMethod(method);
    } else if (methodName.startsWith(SETTER_METHOD_PREFIX)) {
        validateSetterMethod(method);
    } else if (methodName.startsWith(BOOLEAN_GETTER_METHOD_PREFIX)) {
        validateBooleanGetterMethod(method);
    } else {
        throw new IllegalArgumentException("Invalid accessor method, must start with 'get', 'set' or 'is'. " + "Method: " + method);
    }
}

private void validateGetterMethod(Method method) {
    if (method.getReturnType() == Void.TYPE) {
        throw new IllegalArgumentException("Invalid accessor method, must have return value if starts with 'get'. Method: " + method);
    } else if (method.getParameterTypes().length > 0) {
        throw new IllegalArgumentException("Invalid accessor method, must have zero arguments if starts with 'get'. Method: " + method);
    }
}

private void validateSetterMethod(Method method) {
    if (method.getReturnType() != Void.TYPE) {
        throw new IllegalArgumentException("Invalid accessor method, must not have return value if starts with 'set'. Method: " + method);
    } else if (method.getParameterTypes().length != 1) {
        throw new IllegalArgumentException("Invalid accessor method, must have one argument if starts with 'set'. Method: " + method);
    }
}

private void validateBooleanGetterMethod(Method method) {
    if (method.getReturnType() != Boolean.TYPE || !method.getReturnType().isPrimitive()) {
        throw new IllegalArgumentException("Invalid accessor method, must return boolean primitive if starts " + "with 'is'. Method: " + method);
    }
}

private String extractPropertyName(Method method) {
    String methodName = method.getName();
    String propertyNameInAccessorMethod;
    if (methodName.startsWith(GETTER_METHOD_PREFIX)) {
        propertyNameInAccessorMethod = methodName.substring(GETTER_METHOD_PREFIX_LENGTH);
    } else if (methodName.startsWith(SETTER_METHOD_PREFIX)) {
        propertyNameInAccessorMethod = methodName.substring(SETTER_METHOD_PREFIX_LENGTH);
    } else {
        propertyNameInAccessorMethod = methodName.substring(BOOLEAN_GETTER_METHOD_PREFIX_LENGTH);
    }
    if (propertyNameInAccessorMethod.length() == 0 || !Character.isUpperCase(propertyNameInAccessorMethod.charAt(0))) {
        throw new IllegalArgumentException("Invalid accessor method, prefix '" + getPrefix(method) + "' must be followed a non-empty property name, capitalized. Method: " + method);
    }
    return propertyNameInAccessorMethod;
}

private String getPrefix(Method method) {
    String methodName = method.getName();
    if (methodName.startsWith(GETTER_METHOD_PREFIX)) {
        return GETTER_METHOD_PREFIX;
    } else if (methodName.startsWith(SETTER_METHOD_PREFIX)) {
        return SETTER_METHOD_PREFIX;
    } else {
        return BOOLEAN_GETTER_METHOD_PREFIX;
    }
}

