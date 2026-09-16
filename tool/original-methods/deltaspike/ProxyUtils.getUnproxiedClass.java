/**
 * @param currentClass current class
 * @return class of the real implementation
 */
public static Class getUnproxiedClass(Class currentClass) {
    Class unproxiedClass = currentClass;
    while (isProxiedClass(unproxiedClass)) {
        unproxiedClass = unproxiedClass.getSuperclass();
    }
    return unproxiedClass;
}