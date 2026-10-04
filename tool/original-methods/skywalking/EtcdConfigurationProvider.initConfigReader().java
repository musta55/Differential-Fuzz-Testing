@Override
protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
    if (StringUtil.isEmpty(settings.getEndpoints())) {
        throw new ModuleStartException("Etcd endpoints cannot be null or empty.");
    }
    try {
        return new EtcdConfigWatcherRegister(settings);
    } catch (Exception e) {
        throw new ModuleStartException(e.getMessage(), e);
    }
}