@Override
protected void updateData(SummaryInfo info, Sample sample) {
    SummaryInfo overallInfo = getOverallInfo();
    if (overallInfo.getData() == null) {
        overallInfo.setData(new Top5ErrorsSummaryData());
    }
    if (info.getData() == null) {
        info.setData(new Top5ErrorsSummaryData());
    }
    if (!sample.isEmptyController()) {
        aggregateSample(sample, info.getData(), false);
        aggregateSample(sample, overallInfo.getData(), true);
    }
}