protected void notifyWatchers(List<RemoteInstance> remoteInstances) {
    if (log.isDebugEnabled()) {
        log.debug("Notify watchers and update cluster instances:{}", remoteInstances.toString());
    }
    this.clusterWatchers.forEach(clusterWatcher -> clusterWatcher.onClusterNodesChanged(remoteInstances));
}