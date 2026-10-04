@Override
public void run() {
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
    if (DMLScript.STATISTICS && triggered)
        SparkStatistics.incAsyncTriggerCheckpointCount(1);
}