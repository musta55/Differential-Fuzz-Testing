private void compareTime(CompareAssertionResult result) {
    if (compareTime < 0) {
        return;
    }
    long prevTime = -1;
    SampleResult prevResult = null;
    for (SampleResult currentResult : responses) {
        long currentTime = currentResult.getTime();
        if (prevTime != -1) {
            boolean failure = Math.abs(prevTime - currentTime) > compareTime;
            if (failure) {
                markTimeFailure(result, prevResult, prevTime, currentResult, currentTime);
                return;
            }
        }
        prevResult = currentResult;
        prevTime = currentTime;
    }
}