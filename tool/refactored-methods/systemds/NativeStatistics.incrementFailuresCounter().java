public static void incrementFailuresCounter() {
    numFailures.increment();
    LOG.log(Level.SEVERE, "Unexpected ERROR: OOM caused during JNI transfer. Please disable native BLAS by setting environment variable: SYSTEMDS_BLAS=none");
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendDenseStats(StringBuilder sb, String blas) {
    sb.append("Native ").append(blas).append(" calls (dense mult/conv/bwdF/bwdD):\t").append(numLibMatrixMultCalls.longValue()).append("/").append(numConv2dCalls.longValue()).append("/").append(numConv2dBwdFilterCalls.longValue()).append("/").append(numConv2dBwdDataCalls.longValue()).append(".\n");
}

private static void appendSparseStats(StringBuilder sb, String blas) {
    sb.append("Native ").append(blas).append(" calls (sparse conv/bwdF/bwdD):\t").append(numSparseConv2dCalls.longValue()).append("/").append(numSparseConv2dBwdFilterCalls.longValue()).append("/").append(numSparseConv2dBwdDataCalls.longValue()).append(".\n");
}

private static void appendTimes(StringBuilder sb, String blas) {
    sb.append("Native ").append(blas).append(" times (dense mult/conv/bwdF/bwdD):\t").append(String.format("%.3f", libMatrixMultTime.longValue() * 1e-9)).append("/").append(String.format("%.3f", conv2dTime.longValue() * 1e-9)).append("/").append(String.format("%.3f", conv2dBwdFilterTime.longValue() * 1e-9)).append("/").append(String.format("%.3f", conv2dBwdDataTime.longValue() * 1e-9)).append(".\n");
}

