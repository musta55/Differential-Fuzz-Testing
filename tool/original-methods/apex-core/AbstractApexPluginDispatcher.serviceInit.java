@Override
protected void serviceInit(Configuration conf) throws Exception {
    super.serviceInit(conf);
    this.launchConfig = readLaunchConfiguration();
    if (locator != null) {
        Collection<DAGExecutionPlugin> plugins = locator.discoverPlugins(this.launchConfig);
        if (plugins != null) {
            this.plugins.addAll(plugins);
            for (DAGExecutionPlugin plugin : plugins) {
                LOG.info("Detected plugin {}", plugin);
            }
        }
    }
    for (DAGExecutionPlugin plugin : plugins) {
        plugin.setup(new PluginManagerImpl(plugin));
    }
}