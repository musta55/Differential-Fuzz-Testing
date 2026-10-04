@Override
protected ConfigWatcherRegister initConfigReader() throws ModuleStartException {
    validateSettings();
    try {
        return new EtcdConfigWatcherRegister(settings);
    } catch (Exception e) {
        throw new ModuleStartException(e.getMessage(), e);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void validateSettings() throws ModuleStartException {
    if (StringUtil.isEmpty(settings.getEndpoints())) {
        throw new ModuleStartException("Etcd endpoints cannot be null or empty.");
    }
}

