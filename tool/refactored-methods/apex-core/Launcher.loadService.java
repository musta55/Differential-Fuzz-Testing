protected static <T> T loadService(Class<T> clazz) {
    // Load the service using ServiceLoader
    ServiceLoader<T> loader = ServiceLoader.load(clazz);
    Iterator<T> iterator = loader.iterator();
    // Check if there is an implementation available
    if (!iterator.hasNext()) {
        throw new RuntimeException("No implementation for " + clazz);
    }
    // Return the first implementation found
    return iterator.next();
}
// ---- helper method(s) introduced by the refactoring ----
private static <L extends Launcher<?>> L invokeNewInstanceMethod(LaunchMode<L> launchMode) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    Method method = launchMode.clazz.getDeclaredMethod(NEW_INSTANCE_METHOD);
    return (L) method.invoke(null);
}

private static RuntimeException rethrowException(Throwable throwable) {
    return Throwables.propagate(throwable);
}

