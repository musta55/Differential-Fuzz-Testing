public static void addSecurityProvider(Properties properties) {
    properties.keySet().stream().filter(key -> key.toString().matches("security\\.provider(\\.\\d+)?")).sorted(Comparator.comparing(String::valueOf)).map(properties::get).map(Object::toString).forEach(SecurityProviderLoader::addSecurityProvider);
}
// ---- helper method(s) introduced by the refactoring ----
private static Provider instantiateProvider(Class<Provider> providerClass, String config) throws InstantiationException, IllegalAccessException, InvocationTargetException, NoSuchMethodException {
    if (config != null) {
        try {
            Constructor<Provider> constructor = providerClass.getConstructor(String.class);
            return constructor.newInstance(config);
        } catch (NoSuchMethodException e) {
            log.warn("Security Provider {} has no constructor with a single String argument - try to use default constructor.", providerClass);
        }
    }
    return providerClass.getDeclaredConstructor().newInstance();
}

