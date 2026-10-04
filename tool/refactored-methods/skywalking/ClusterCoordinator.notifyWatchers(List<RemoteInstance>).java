protected void notifyWatchers(List<RemoteInstance> remoteInstances) {
    logNotification(remoteInstances);
    this.clusterWatchers.forEach(clusterWatcher -> clusterWatcher.onClusterNodesChanged(remoteInstances));
}
// ---- helper method(s) introduced by the refactoring ----
private void logNotification(List<RemoteInstance> remoteInstances) {
    if (log.isDebugEnabled()) {
        log.debug("Notify watchers and update cluster instances:{}", remoteInstances.toString());
    }
}

