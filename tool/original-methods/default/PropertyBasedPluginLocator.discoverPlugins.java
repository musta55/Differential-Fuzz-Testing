@Override
public Set<T> discoverPlugins(Configuration conf) {
    Set<T> detectedPlugins = new LinkedHashSet<>();
    String classNamesStr = conf.get(this.propertyName);
    if (StringUtils.isBlank(classNamesStr)) {
        return detectedPlugins;
    }
    Set<String> classNames = new LinkedHashSet<>();
    Collections.addAll(classNames, classNamesStr.split(","));
    for (String className : classNames) {
        try {
            Class<?> plugin = StramUtils.classForName(className, Object.class);
            if (klass.isAssignableFrom(plugin)) {
                detectedPlugins.add(StramUtils.newInstance(plugin.asSubclass(klass)));
            } else {
                LOG.info("Skipping loading {} incompatible with {}", className, klass);
            }
        } catch (IllegalArgumentException e) {
            LOG.warn("Could not load plugin {}", className, e);
        }
    }
    return detectedPlugins;
}