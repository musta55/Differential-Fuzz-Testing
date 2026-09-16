public static boolean isInterfaceProxy(Class<?> proxyClass) {
    Class<?>[] interfaces = proxyClass.getInterfaces();
    if (Proxy.class.equals(proxyClass.getSuperclass()) && interfaces != null && interfaces.length > 0) {
        return true;
    }
    if (proxyClass.getSuperclass() != null && !proxyClass.getSuperclass().equals(Object.class)) {
        return false;
    }
    if (proxyClass.getName().contains("$$")) {
        for (Class<?> currentInterface : interfaces) {
            if (proxyClass.getName().startsWith(currentInterface.getName())) {
                return true;
            }
        }
    }
    return false;
}