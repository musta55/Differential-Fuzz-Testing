public static boolean isInterfaceProxy(Class<?> proxyClass) {
    Class<?>[] interfaces = proxyClass.getInterfaces();
    if (Proxy.class.equals(proxyClass.getSuperclass()) && interfaces != null && interfaces.length > 0) {
        return true;
    }
    if (!Object.class.equals(proxyClass.getSuperclass())) {
        return false;
    }
    return proxyClass.getName().contains("$$") && hasMatchingInterface(proxyClass, interfaces);
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

