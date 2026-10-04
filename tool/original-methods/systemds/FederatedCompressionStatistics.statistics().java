public static String statistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("Federated Compression Statistics (Worker):\n");
    sb.append("Encoding:\n");
    sb.append(" Total encoding millis: " + totalEncodingMillis.longValue() + "\n");
    sb.append(" Total pre-encoding size: " + totalEncodingBeforeSize.longValue() + "\n");
    sb.append(" Total post-encoding size: " + totalEncodingAfterSize.longValue() + "\n");
    sb.append(" Compression ratio: " + (double) (totalEncodingAfterSize.longValue()) / ((double) totalEncodingBeforeSize.longValue()) + "\n");
    sb.append("Decoding:\n");
    sb.append(" Total decoding millis: " + totalDecodingMillis.longValue() + "\n");
    sb.append(" Total pre-decoding size: " + totalDecodingBeforeSize.longValue() + "\n");
    sb.append(" Total post-decoding size: " + totalDecodingAfterSize.longValue() + "\n");
    sb.append(" Compression ratio: " + (double) (totalDecodingBeforeSize.longValue()) / ((double) totalDecodingAfterSize.longValue()) + "\n");
    return sb.toString();
}