@Override
protected void serviceStop() throws Exception {
    for (DAGExecutionPlugin plugin : plugins) {
        try {
            plugin.teardown();
        } catch (Exception e) {
            LOG.warn("Exception during {} teardown", plugin, e);
        }
    }
    super.serviceStop();
}