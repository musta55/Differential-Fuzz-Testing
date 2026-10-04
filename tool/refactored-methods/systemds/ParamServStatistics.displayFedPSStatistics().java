private static String displayFedPSStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append(formatTime("PS fed data partitioning time:", fedDataPartitioningTime.doubleValue()));
    sb.append(formatTime("PS fed comm time (cum):", fedCommunicationTime.doubleValue()));
    sb.append(formatTime("PS fed worker comp time (cum):", fedWorkerComputingTime.doubleValue()));
    sb.append(formatTime("PS fed grad. weigh. time (cum):", fedGradientWeightingTime.doubleValue()));
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatTime(String label, double milliseconds) {
    return String.format("%s\t%.3f secs.\n", label, milliseconds / 1000);
}

private static String formatCount(String label, long count) {
    return String.format("%s\t%d.\n", label, count);
}

