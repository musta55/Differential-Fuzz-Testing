/**
 * @return a string for the sampleResult Title
 */
private String getTitle() {
    return getName();
}
// ---- helper method(s) introduced by the refactoring ----
/*
     * Helper method
     */
private void debugTrace(String s) {
    if (log.isDebugEnabled()) {
        log.debug("{} ({}) {} {} {}", Thread.currentThread().getName(), classCount.get(), getTitle(), s, this.toString());
    }
}

