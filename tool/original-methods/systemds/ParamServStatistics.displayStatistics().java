public static String displayStatistics() {
    if (numWorkers.longValue() > 0) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Paramserv total execution time:\t%.3f secs.\n", executionTime.doubleValue() / 1000));
        sb.append(String.format("Paramserv total num workers:\t%d.\n", numWorkers.longValue()));
        sb.append(String.format("Paramserv setup time:\t\t%.3f secs.\n", setupTime.doubleValue() / 1000));
        if (fedDataPartitioningTime.longValue() > 0) {
            //if data partitioning happens this is the federated case
            sb.append(displayFedPSStatistics());
            sb.append(String.format("PS fed global model agg time:\t%.3f secs.\n", aggregationTime.doubleValue() / 1000));
        } else {
            sb.append(String.format("Paramserv grad compute time:\t%.3f secs.\n", gradientComputeTime.doubleValue() / 1000));
            sb.append(String.format("Paramserv model update time:\t%.3f/%.3f secs.\n", localModelUpdateTime.doubleValue() / 1000, aggregationTime.doubleValue() / 1000));
            sb.append(String.format("Paramserv model broadcast time:\t%.3f secs.\n", modelBroadcastTime.doubleValue() / 1000));
            sb.append(String.format("Paramserv batch slice time:\t%.3f secs.\n", batchIndexTime.doubleValue() / 1000));
            sb.append(String.format("Paramserv RPC request time:\t%.3f secs.\n", rpcRequestTime.doubleValue() / 1000));
        }
        sb.append(String.format("Paramserv valdiation time:\t%.3f secs.\n", validationTime.doubleValue() / 1000));
        return sb.toString();
    }
    return "";
}