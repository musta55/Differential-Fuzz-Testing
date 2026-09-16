private void compareContent(CompareAssertionResult result) {
    if (!compareContent) {
        return;
    }
    String prevContent = null;
    SampleResult prevResult = null;
    for (SampleResult currentResult : responses) {
        String currentContent = filterString(currentResult.getResponseDataAsString());
        if (prevContent != null && !prevContent.equals(currentContent)) {
            markContentFailure(result, prevContent, prevResult, currentResult, currentContent);
            return;
        }
        prevResult = currentResult;
        prevContent = currentContent;
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static void appendIfNotNull(StringBuilder buf, String str) {
    if (str != null) {
        buf.append(str.trim());
    }
}

