@Override
public Boolean isActivated(Class<? extends Deactivatable> targetClass) {
    final String key = KEY_PREFIX + targetClass.getName();
    final String value = ConfigResolver.getPropertyValue(key);
    if (value == null) {
        return null;
    }
    logDeactivationSetting(key, value);
    return !Boolean.valueOf(value);
}
// ---- helper method(s) introduced by the refactoring ----
private void logDeactivationSetting(String key, String value) {
    if (LOG.isLoggable(Level.FINE)) {
        LOG.log(Level.FINE, "Deactivation setting for {0} found to be {1} based on configuration.", new Object[] { key, value });
    }
}

