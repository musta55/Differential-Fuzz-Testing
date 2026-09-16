@Override
public void releaseConfig(ClassLoader cl) {
    ConfigImpl oldConfig = configs.remove(cl);
    if (oldConfig != null) {
        oldConfig.release();
    }
    // And remove all the children as well.
    // This will e.g happen in EAR scenarios
    removeChildConfigs(cl);
}
// ---- helper method(s) introduced by the refactoring ----
private void removeChildConfigs(ClassLoader cl) {
    Iterator<Map.Entry<ClassLoader, ConfigImpl>> it = configs.entrySet().iterator();
    while (it.hasNext()) {
        Map.Entry<ClassLoader, ConfigImpl> cfgEntry = it.next();
        if (isChildClassLoader(cl, cfgEntry.getKey())) {
            cfgEntry.getValue().release();
            it.remove();
        }
    }
}

