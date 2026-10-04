public Map<String, String> configMapData() {
    Map<String, String> configMapData = new HashMap<>();
    if (configMapLister != null) {
        final List<ConfigMap> list = configMapLister.list();
        if (list != null) {
            list.forEach(cf -> {
                Map<String, String> data = cf.getData();
                if (data == null) {
                    return;
                }
                configMapData.putAll(data);
            });
        }
    }
    return configMapData;
}