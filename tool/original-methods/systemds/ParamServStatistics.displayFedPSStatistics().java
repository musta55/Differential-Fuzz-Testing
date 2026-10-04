private static String displayFedPSStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append(String.format("PS fed data partitioning time:\t%.3f secs.\n", fedDataPartitioningTime.doubleValue() / 1000));
    sb.append(String.format("PS fed comm time (cum):\t\t%.3f secs.\n", fedCommunicationTime.doubleValue() / 1000));
    sb.append(String.format("PS fed worker comp time (cum):\t%.3f secs.\n", fedWorkerComputingTime.doubleValue() / 1000));
    sb.append(String.format("PS fed grad. weigh. time (cum):\t%.3f secs.\n", fedGradientWeightingTime.doubleValue() / 1000));
    return sb.toString();
}