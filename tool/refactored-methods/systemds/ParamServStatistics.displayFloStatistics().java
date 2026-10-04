public static String displayFloStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append(formatTime("PS fed network time (cum):", fedNetworkTime.doubleValue()));
    sb.append(formatTime("PS fed agg time:", fedAggregation.doubleValue()));
    sb.append(formatTime("Paramserv grad compute time:", gradientComputeTime.doubleValue()));
    sb.append(formatTime("HE PS encryption time:", heEncryption.doubleValue()));
    sb.append(formatTime("HE PS accumulation time:", heAccumulation.doubleValue()));
    sb.append(formatTime("HE PS partial decryption time:", hePartialDecryption.doubleValue()));
    sb.append(formatTime("HE PS decryption time:", heDecryption.doubleValue()));
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatTime(String label, double milliseconds) {
    return String.format("%s\t%.3f secs.\n", label, milliseconds / 1000);
}

private static String formatCount(String label, long count) {
    return String.format("%s\t%d.\n", label, count);
}

