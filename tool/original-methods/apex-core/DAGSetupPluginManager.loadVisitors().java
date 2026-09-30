private void loadVisitors() {
    if (!plugins.isEmpty()) {
        return;
    }
    PropertyBasedPluginLocator<DAGSetupPlugin> locator = new PropertyBasedPluginLocator<>(DAGSetupPlugin.class, DAGSETUP_PLUGINS_CONF_KEY);
    this.plugins.addAll(locator.discoverPlugins(conf));
}