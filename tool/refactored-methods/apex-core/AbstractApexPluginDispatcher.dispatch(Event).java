@Override
public void dispatch(Event event) {
    handleDagChange(event);
    dispatchExecutionEvents(event);
}
// ---- helper method(s) introduced by the refactoring ----
private void discoverAndSetupPlugins() {
    if (locator != null) {
        Collection<DAGExecutionPlugin> discoveredPlugins = locator.discoverPlugins(this.launchConfig);
        if (discoveredPlugins != null) {
            this.plugins.addAll(discoveredPlugins);
            for (DAGExecutionPlugin plugin : discoveredPlugins) {
                LOG.info("Detected plugin {}", plugin);
            }
        }
    }
    setupPlugins();
}

private void setupPlugins() {
    for (DAGExecutionPlugin plugin : plugins) {
        plugin.setup(new PluginManagerImpl(plugin));
    }
}

private void teardownPlugins() {
    for (DAGExecutionPlugin plugin : plugins) {
        try {
            plugin.teardown();
        } catch (Exception e) {
            LOG.warn("Exception during {} teardown", plugin, e);
        }
    }
}

private void handleDagChange(Event event) {
    if (event.getType() == ApexPluginDispatcher.DAG_CHANGE) {
        clonedDAG = SerializationUtils.clone(((DAGChangeEvent) event).dag);
    }
}

private void dispatchExecutionEvents(Event event) {
    if (!plugins.isEmpty() && (event instanceof DAGExecutionEvent)) {
        dispatchExecutionEvent((DAGExecutionEvent) event);
    }
}

