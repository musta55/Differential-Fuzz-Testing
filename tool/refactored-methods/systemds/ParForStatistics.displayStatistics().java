public static String displayStatistics() {
    if (optCount.longValue() > 0) {
        StringBuilder sb = new StringBuilder();
        appendOptimizedLoops(sb);
        appendOptimizeTime(sb);
        appendInitializeTime(sb);
        appendResultMergeTime(sb);
        appendTotalUpdateInPlace(sb);
        return sb.toString();
    }
    return "";
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendOptimizedLoops(StringBuilder sb) {
    sb.append("ParFor loops optimized:\t\t").append(getOptCount()).append(".\n");
}

private static void appendOptimizeTime(StringBuilder sb) {
    sb.append("ParFor optimize time:\t\t").append(String.format("%.3f", ((double) getOptTime()) / 1000)).append(" sec.\n");
}

private static void appendInitializeTime(StringBuilder sb) {
    sb.append("ParFor initialize time:\t\t").append(String.format("%.3f", ((double) getInitTime()) / 1000)).append(" sec.\n");
}

private static void appendResultMergeTime(StringBuilder sb) {
    sb.append("ParFor result merge time:\t").append(String.format("%.3f", ((double) getMergeTime()) / 1000)).append(" sec.\n");
}

private static void appendTotalUpdateInPlace(StringBuilder sb) {
    sb.append("ParFor total update in-place:\t").append(Statistics.getTotalUIPVar()).append("/").append(Statistics.getTotalLixUIP()).append("/").append(Statistics.getTotalLix()).append("\n");
}

