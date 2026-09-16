private static void createStatistic(Map<? super String, ? super SamplingStatistic> statistics, MapResultData resultData) {
    LOGGER.debug("Creating statistics for result data:{}", resultData);
    SamplingStatistic statistic = new SamplingStatistic();
    ListResultData listResultData = (ListResultData) resultData.getResult("data");
    statistic.setTransaction((String) ((ValueResultData) listResultData.get(0)).getValue());
    statistic.setSampleCount((Long) ((ValueResultData) listResultData.get(1)).getValue());
    statistic.setErrorCount((Long) ((ValueResultData) listResultData.get(2)).getValue());
    statistic.setErrorPct(((Double) ((ValueResultData) listResultData.get(3)).getValue()).floatValue());
    statistic.setMeanResTime((Double) ((ValueResultData) listResultData.get(4)).getValue());
    statistic.setMinResTime((Long) ((ValueResultData) listResultData.get(5)).getValue());
    statistic.setMaxResTime((Long) ((ValueResultData) listResultData.get(6)).getValue());
    statistic.setMedianResTime((Double) ((ValueResultData) listResultData.get(7)).getValue());
    statistic.setPct1ResTime((Double) ((ValueResultData) listResultData.get(8)).getValue());
    statistic.setPct2ResTime((Double) ((ValueResultData) listResultData.get(9)).getValue());
    statistic.setPct3ResTime((Double) ((ValueResultData) listResultData.get(10)).getValue());
    statistic.setThroughput((Double) ((ValueResultData) listResultData.get(11)).getValue());
    statistic.setReceivedKBytesPerSec((Double) ((ValueResultData) listResultData.get(12)).getValue());
    statistic.setSentKBytesPerSec((Double) ((ValueResultData) listResultData.get(13)).getValue());
    statistics.put(statistic.getTransaction(), statistic);
}