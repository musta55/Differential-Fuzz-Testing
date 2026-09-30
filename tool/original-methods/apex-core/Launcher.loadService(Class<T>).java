protected static <T> T loadService(Class<T> clazz) {
    ServiceLoader<T> loader = ServiceLoader.load(clazz);
    Iterator<T> impl = loader.iterator();
    if (!impl.hasNext()) {
        throw new RuntimeException("No implementation for " + clazz);
    }
    return impl.next();
}