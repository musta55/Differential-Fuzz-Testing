private void compareContent(CompareAssertionResult result) {
    if (!compareContent) {
        return;
    }
    String prevContent = null;
    SampleResult prevResult = null;
    for (SampleResult currentResult : responses) {
        String currentContent = currentResult.getResponseDataAsString();
        currentContent = filterString(currentContent);
        if (prevContent != null) {
            boolean failure = !prevContent.equals(currentContent);
            if (failure) {
                markContentFailure(result, prevContent, prevResult, currentResult, currentContent);
                return;
            }
        }
        prevResult = currentResult;
        prevContent = currentContent;
    }
}