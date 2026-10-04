@Override
public void run() {
    try {
        SparkExecutionContext sec = (SparkExecutionContext) _ec;
        sec.setBroadcastHandle(_broadcastMO);
    } catch (Exception e) {
        e.printStackTrace();
    }
    //TODO: Count only if successful (owned lock)
    if (DMLScript.STATISTICS)
        SparkStatistics.incAsyncBroadcastCount(1);
}