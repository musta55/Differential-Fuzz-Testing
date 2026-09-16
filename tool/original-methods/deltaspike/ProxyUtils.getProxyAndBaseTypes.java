public static List<Class<?>> getProxyAndBaseTypes(Class<?> proxyClass) {
    List<Class<?>> result = new ArrayList<Class<?>>();
    result.add(proxyClass);
    if (isInterfaceProxy(proxyClass)) {
        for (Class<?> currentInterface : proxyClass.getInterfaces()) {
            if (proxyClass.getName().startsWith(currentInterface.getName())) {
                result.add(currentInterface);
            }
        }
    } else {
        Class unproxiedClass = proxyClass.getSuperclass();
        result.add(unproxiedClass);
        while (isProxiedClass(unproxiedClass)) {
            unproxiedClass = unproxiedClass.getSuperclass();
            result.add(unproxiedClass);
        }
    }
    return result;
}