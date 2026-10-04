public static String displayStatistics() {
    if (numWorkers.longValue() > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append(formatTime("Paramserv total execution time:", executionTime.doubleValue()));
        sb.append(formatCount("Paramserv total num workers:", numWorkers.longValue()));
        sb.append(formatTime("Paramserv setup time:", setupTime.doubleValue()));
        if (fedDataPartitioningTime.longValue() > 0) {
            //if data partitioning happens this is the federated case
            sb.append(displayFedPSStatistics());
            sb.append(formatTime("PS fed global model agg time:", aggregationTime.doubleValue()));
        } else {
            sb.append(formatTime("Paramserv grad compute time:", gradientComputeTime.doubleValue()));
            sb.append(formatTime("Paramserv model update time:", localModelUpdateTime.doubleValue()));
            sb.append(formatTime("Paramserv model broadcast time:", modelBroadcastTime.doubleValue()));
            sb.append(formatTime("Paramserv batch slice time:", batchIndexTime.doubleValue()));
            sb.append(formatTime("Paramserv RPC request time:", rpcRequestTime.doubleValue()));
        }
        sb.append(formatTime("Paramserv validation time:", validationTime.doubleValue()));
        return sb.toString();
    }
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatTime(String label, double milliseconds) {
    return String.format("%s\t%.3f secs.\n", label, milliseconds / 1000);
}

private static String formatCount(String label, long count) {
    return String.format("%s\t%d.\n", label, count);
}

