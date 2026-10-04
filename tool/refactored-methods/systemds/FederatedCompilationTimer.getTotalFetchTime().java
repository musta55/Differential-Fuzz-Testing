private static long getTotalFetchTime() {
    return times.stream().filter(t -> t.is("PrivFetch")).mapToLong(TimeEntry::getDuration).sum();
}
// ---- helper method(s) introduced by the refactoring ----
private static String formatNanoToSeconds(long nanoSeconds) {
    return String.format("%.3f", nanoSeconds * 1e-9) + " sec.";
}

private static String buildBasicCompilationTimeString(long basicCompileTime) {
    return "Basic Compilation Time:\t\t" + formatNanoToSeconds(basicCompileTime) + "\n";
}

private static String buildTotalPrivacyFetchTimeString(long totalFetchTime) {
    return "Total Privacy Fetch Time:\t" + formatNanoToSeconds(totalFetchTime) + "\n";
}

private static String buildPrivacyPropagationTimeString(long privPropagationTime) {
    return "Privacy Propagation Time:\t" + formatNanoToSeconds(privPropagationTime) + "\n";
}

private static String buildPlanEnumerationTimeString(long enumerationDuration) {
    return "Plan Enumeration Time:\t\t" + formatNanoToSeconds(enumerationDuration) + "\n";
}

private static String buildPlanSelectionTimeString(long selectionDuration) {
    return "Plan Selection Time:\t\t" + formatNanoToSeconds(selectionDuration) + "\n";
}

