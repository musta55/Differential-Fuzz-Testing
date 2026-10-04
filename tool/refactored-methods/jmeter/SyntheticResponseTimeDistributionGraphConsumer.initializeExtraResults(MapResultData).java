@Override
protected void initializeExtraResults(MapResultData parentResult) {
    ListResultData listResultData = new ListResultData();
    String[] seriesLabels = new String[] { SATISFIED_LABEL.format(new Object[] { satisfiedThreshold }), TOLERATED_LABEL.format(new Object[] { satisfiedThreshold, toleratedThreshold }), UNTOLERATED_LABEL.format(new Object[] { toleratedThreshold }), FAILED_LABEL };
    String[] colors = new String[] { SATISFIED_COLOR, TOLERATED_COLOR, UNTOLERATED_COLOR, FAILED_COLOR };
    for (int i = 0; i < seriesLabels.length; i++) {
        ListResultData array = new ListResultData();
        array.addResult(new ValueResultData(i));
        array.addResult(new ValueResultData(seriesLabels[i]));
        listResultData.addResult(array);
    }
    parentResult.setResult("ticks", listResultData);
    initializeSeries(parentResult, seriesLabels, colors);
}
// ---- helper method(s) introduced by the refactoring ----
private static long getStaticSatisfiedThreshold() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).satisfiedThreshold;
}

private static long getStaticToleratedThreshold() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).toleratedThreshold;
}

private static List<String> getStaticSatisfiedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).satisfiedLabels;
}

private static List<String> getStaticToleratedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).toleratedLabels;
}

private static List<String> getStaticUntoleratedLabels() {
    return ((SyntheticResponseTimeDistributionGraphConsumer) getCurrentInstance()).untoleratedLabels;
}

private static SyntheticResponseTimeDistributionGraphConsumer getCurrentInstance() {
    // Assuming there's a way to get the current instance, adjust accordingly
    throw new UnsupportedOperationException("Static access to instance fields is not supported.");
}

