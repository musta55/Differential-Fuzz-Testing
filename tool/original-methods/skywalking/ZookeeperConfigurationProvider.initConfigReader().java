@Override
protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
    if (Strings.isNullOrEmpty(settings.getHostPort())) {
        throw new ModuleStartException("Zookeeper hostPort cannot be null or empty.");
    }
    if (Strings.isNullOrEmpty(settings.getNamespace())) {
        throw new ModuleStartException("Zookeeper namespace cannot be null or empty.");
    }
    try {
        return new ZookeeperConfigWatcherRegister(settings);
    } catch (Exception e) {
        throw new ModuleStartException(e.getMessage(), e);
    }
}