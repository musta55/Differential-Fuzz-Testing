private void compareTime(CompareAssertionResult result) {
    if (compareTime < 0) {
        return;
    }
    long prevTime = -1;
    SampleResult prevResult = null;
    for (SampleResult currentResult : responses) {
        long currentTime = currentResult.getTime();
        if (prevTime != -1 && Math.abs(prevTime - currentTime) > compareTime) {
            markTimeFailure(result, prevResult, prevTime, currentResult, currentTime);
            return;
        }
        prevResult = currentResult;
        prevTime = currentTime;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfNotNull(StringBuilder buf, String str) {
    if (str != null) {
        buf.append(str.trim());
    }
}

