@Override
protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods) {
    ArrayList<Method> methods = new ArrayList<>();
    for (Method method : allMethods) {
        if (Modifier.isAbstract(method.getModifiers())) {
            methods.add(method);
        }
    }
    return methods;
}