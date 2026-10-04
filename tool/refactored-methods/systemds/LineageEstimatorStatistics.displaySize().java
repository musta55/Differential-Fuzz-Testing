public static String displaySize() {
    //size of all cached reusable intermediates/size of reused intermediates/cache size
    StringBuilder sb = new StringBuilder();
    sb.append(formatSizeInMB(LineageEstimator._totReusableSize));
    sb.append("/");
    sb.append(formatSizeInMB(LineageEstimator._totReusedSize));
    sb.append("/");
    sb.append(formatSizeInMB(LineageEstimator.CACHE_LIMIT));
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatTimeInSeconds(double nanoseconds) {
    return String.format("%.3f", nanoseconds * 1e-9);
}

private static String formatSizeInMB(double bytes) {
    return String.format("%.3f", bytes / (1024 * 1024));
}

private static String formatInstructionLine(int index, String opcode, double time, long count, int decimalIndex) {
    return String.valueOf(index) + // 4-length(i) spaces
    String.format("%" + (4 - String.valueOf(index).length()) + "s", "") + opcode + // 15 - length(opcode) spaces
    String.format("%" + (15 - opcode.length()) + "s", "") + String.format("%.3f", time * 1e-3) + // 8 - length(time upto '.') spaces
    String.format("%" + (8 - (decimalIndex + 3)) + "s", "") + count + "\n";
}

