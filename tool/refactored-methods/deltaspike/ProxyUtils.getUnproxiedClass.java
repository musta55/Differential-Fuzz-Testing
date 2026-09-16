/**
 * @param currentClass current class
 * @return class of the real implementation
 */
public static Class getUnproxiedClass(Class currentClass) {
    while (isProxiedClass(currentClass)) {
        currentClass = currentClass.getSuperclass();
    }
    return currentClass;
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

