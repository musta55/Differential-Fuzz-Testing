public static String statistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("Federated Compression Statistics (Worker):\n");
    appendEncodingStats(sb);
    appendDecodingStats(sb);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendEncodingStats(StringBuilder sb) {
    sb.append("Encoding:\n");
    sb.append(" Total encoding millis: ").append(totalEncodingMillis.longValue()).append("\n");
    sb.append(" Total pre-encoding size: ").append(totalEncodingBeforeSize.longValue()).append("\n");
    sb.append(" Total post-encoding size: ").append(totalEncodingAfterSize.longValue()).append("\n");
    sb.append(" Compression ratio: ").append((double) totalEncodingAfterSize.longValue() / totalEncodingBeforeSize.longValue()).append("\n");
}

private static void appendDecodingStats(StringBuilder sb) {
    sb.append("Decoding:\n");
    sb.append(" Total decoding millis: ").append(totalDecodingMillis.longValue()).append("\n");
    sb.append(" Total pre-decoding size: ").append(totalDecodingBeforeSize.longValue()).append("\n");
    sb.append(" Total post-decoding size: ").append(totalDecodingAfterSize.longValue()).append("\n");
    sb.append(" Compression ratio: ").append((double) totalDecodingBeforeSize.longValue() / totalDecodingAfterSize.longValue()).append("\n");
}

