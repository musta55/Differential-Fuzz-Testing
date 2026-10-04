@Override
protected void initializeExtraResults(MapResultData parentResult) {
    ListResultData listResultData = new ListResultData();
    String[] seriesLabels = new String[] { SATISFIED_LABEL.format(new Object[] { getSatisfiedThreshold() }), TOLERATED_LABEL.format(new Object[] { getSatisfiedThreshold(), getToleratedThreshold() }), UNTOLERATED_LABEL.format(new Object[] { getToleratedThreshold() }), FAILED_LABEL };
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