private static void appendResultDetails(StringBuilder buf, SampleResult result) {
    appendIfNotNull(buf, result.getSamplerData());
    //$NON-NLS-1$
    buf.append("\n");
    appendIfNotNull(buf, result.getRequestHeaders());
    //$NON-NLS-1$
    buf.append("\n\n");
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfNotNull(StringBuilder buf, String str) {
    if (str != null) {
        buf.append(str.trim());
    }
}

