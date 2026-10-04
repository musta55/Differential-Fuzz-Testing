@Override
public void run() {
    boolean prefetched = false;
    MatrixBlock mb = null;
    long t1 = System.nanoTime();
    synchronized (_prefetchMO) {
        // Having this check inside the critical section
        // safeguards against concurrent rmVar.
        if (_prefetchMO.isPendingRDDOps() || _prefetchMO.isDeviceToHostCopy() || _prefetchMO.isFederated()) {
            // TODO: Add robust runtime constraints for federated prefetch
            // Execute and bring the result to local
            mb = _prefetchMO.acquireReadAndRelease();
            prefetched = true;
        }
    }
    // Save the collected intermediate in the lineage cache
    if (_inputLi != null && mb != null)
        LineageCache.putValueAsyncOp(_inputLi, _prefetchMO, mb, t1);
    if (DMLScript.STATISTICS && prefetched) {
        if (_prefetchMO.isFederated())
            FederatedStatistics.incAsyncPrefetchCount(1);
        else
            SparkStatistics.incAsyncPrefetchCount(1);
    }
}