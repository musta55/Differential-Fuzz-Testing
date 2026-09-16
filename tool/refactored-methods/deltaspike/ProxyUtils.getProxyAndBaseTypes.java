public static List<Class<?>> getProxyAndBaseTypes(Class<?> proxyClass) {
    List<Class<?>> result = new ArrayList<>();
    result.add(proxyClass);
    if (isInterfaceProxy(proxyClass)) {
        for (Class<?> currentInterface : proxyClass.getInterfaces()) {
            if (proxyClass.getName().startsWith(currentInterface.getName())) {
                result.add(currentInterface);
            }
        }
    } else {
        addBaseTypes(result, proxyClass);
    }
    return result;
}
// ---- helper method(s) introduced by the refactoring ----
private static void addBaseTypes(List<Class<?>> result, Class<?> proxyClass) {
    Class<?> unproxiedClass = proxyClass.getSuperclass();
    result.add(unproxiedClass);
    while (isProxiedClass(unproxiedClass)) {
        unproxiedClass = unproxiedClass.getSuperclass();
        result.add(unproxiedClass);
    }
}

private static boolean hasMatchingInterface(Class<?> proxyClass, Class<?>[] interfaces) {
    for (Class<?> currentInterface : interfaces) {
        if (proxyClass.getName().startsWith(currentInterface.getName())) {
            return true;
        }
    }
    return false;
}

