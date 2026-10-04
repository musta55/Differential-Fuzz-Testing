public Map<String, String> configMapData() {
    Map<String, String> configMapData = new HashMap<>();
    List<ConfigMap> configMaps = getConfigMaps();
    if (configMaps != null) {
        for (ConfigMap configMap : configMaps) {
            addConfigMapData(configMapData, configMap);
        }
    }
    return configMapData;
}
// ---- helper method(s) introduced by the refactoring ----
private List<ConfigMap> getConfigMaps() {
    return configMapLister != null ? configMapLister.list() : null;
}

private void addConfigMapData(Map<String, String> configMapData, ConfigMap configMap) {
    Map<String, String> data = configMap.getData();
    if (data != null) {
        configMapData.putAll(data);
    }
}

