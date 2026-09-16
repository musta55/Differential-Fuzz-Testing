public MethodPropertyImpl(Method method) {
    final String accessorMethodPrefix;
    final String propertyNameInAccessorMethod;
    if (method.getName().startsWith(GETTER_METHOD_PREFIX)) {
        if (method.getReturnType() == Void.TYPE) {
            throw new IllegalArgumentException("Invalid accessor method, must have return value if starts with 'get'. Method: " + method);
        } else if (method.getParameterTypes().length > 0) {
            throw new IllegalArgumentException("Invalid accessor method, must have zero arguments if starts with 'get'. Method: " + method);
        }
        propertyNameInAccessorMethod = method.getName().substring(GETTER_METHOD_PREFIX_LENGTH);
        accessorMethodPrefix = GETTER_METHOD_PREFIX;
    } else if (method.getName().startsWith(SETTER_METHOD_PREFIX)) {
        if (method.getReturnType() != Void.TYPE) {
            throw new IllegalArgumentException("Invalid accessor method, must not have return value if starts with 'set'. Method: " + method);
        } else if (method.getParameterTypes().length != 1) {
            throw new IllegalArgumentException("Invalid accessor method, must have one argument if starts with 'set'. Method: " + method);
        }
        propertyNameInAccessorMethod = method.getName().substring(SETTER_METHOD_PREFIX_LENGTH);
        accessorMethodPrefix = SETTER_METHOD_PREFIX;
    } else if (method.getName().startsWith(BOOLEAN_GETTER_METHOD_PREFIX)) {
        if (method.getReturnType() != Boolean.TYPE || !method.getReturnType().isPrimitive()) {
            throw new IllegalArgumentException("Invalid accessor method, must return boolean primitive if starts " + "with 'is'. Method: " + method);
        }
        propertyNameInAccessorMethod = method.getName().substring(BOOLEAN_GETTER_METHOD_PREFIX_LENGTH);
        accessorMethodPrefix = BOOLEAN_GETTER_METHOD_PREFIX;
    } else {
        throw new IllegalArgumentException("Invalid accessor method, must start with 'get', 'set' or 'is'. " + "Method: " + method);
    }
    if (propertyNameInAccessorMethod.length() == 0 || !Character.isUpperCase(propertyNameInAccessorMethod.charAt(0))) {
        throw new IllegalArgumentException("Invalid accessor method, prefix '" + accessorMethodPrefix + "' must be followed a non-empty property name, capitalized. Method: " + method);
    }
    this.propertyName = Introspector.decapitalize(propertyNameInAccessorMethod);
    this.getterMethod = getGetterMethod(method.getDeclaringClass(), propertyName);
    this.setterMethod = getSetterMethod(method.getDeclaringClass(), propertyName);
}