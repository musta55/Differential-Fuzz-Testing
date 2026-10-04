@Override
public void run() {
    boolean triggered = triggerCheckpointIfNecessary();
    if (DMLScript.STATISTICS && triggered)
        SparkStatistics.incAsyncTriggerCheckpointCount(1);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean triggerCheckpointIfNecessary() {
    boolean triggered = false;
    synchronized (_remoteOperationsRoot) {
        // FIXME: Handle double execution
        if (_remoteOperationsRoot.isPendingRDDOps()) {
            JavaPairRDD<?, ?> rdd = _remoteOperationsRoot.getRDDHandle().getRDD();
            rdd.persist(Checkpoint.DEFAULT_STORAGE_LEVEL).count();
            _remoteOperationsRoot.getRDDHandle().setCheckpointRDD(true);
            triggered = true;
        }
    }
    return triggered;
}

