@Override
public void run() {
    boolean success = triggerBroadcast();
    if (success && DMLScript.STATISTICS) {
        incrementBroadcastStatistics();
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean triggerBroadcast() {
    try {
        SparkExecutionContext sec = (SparkExecutionContext) _ec;
        sec.setBroadcastHandle(_broadcastMO);
        return true;
    } catch (Exception e) {
        e.printStackTrace();
        return false;
    }
}

private void incrementBroadcastStatistics() {
    SparkStatistics.incAsyncBroadcastCount(1);
}

