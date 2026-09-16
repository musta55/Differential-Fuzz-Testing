@Override
protected ArrayList<Method> getDelegateMethods(Class<?> targetClass, ArrayList<Method> allMethods) {
    ArrayList<Method> methods = new ArrayList<>();
    Iterator<Method> it = allMethods.iterator();
    while (it.hasNext()) {
        Method method = it.next();
        if (Modifier.isAbstract(method.getModifiers())) {
            methods.add(method);
        }
    }
    return methods;
}