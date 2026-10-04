private static void createStatistic(Map<? super String, ? super SamplingStatistic> statistics, MapResultData resultData) {
    LOGGER.debug("Creating statistics for result data:{}", resultData);
    SamplingStatistic statistic = new SamplingStatistic();
    ListResultData listResultData = (ListResultData) resultData.getResult("data");
    String[] keys = { "transaction", "sampleCount", "errorCount", "errorPct", "meanResTime", "minResTime", "maxResTime", "medianResTime", "pct1ResTime", "pct2ResTime", "pct3ResTime", "throughput", "receivedKBytesPerSec", "sentKBytesPerSec" };
    Class<?>[] types = { String.class, Long.class, Long.class, Double.class, Double.class, Long.class, Long.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class, Double.class };
    for (int i = 0; i < keys.length; i++) {
        ValueResultData valueResultData = (ValueResultData) listResultData.get(i);
        Object value = valueResultData.getValue();
        if (types[i] == Double.class) {
            value = ((Double) value).floatValue();
        }
        setProperty(statistic, keys[i], value);
    }
    statistics.put(statistic.getTransaction(), statistic);
}
// ---- helper method(s) introduced by the refactoring ----
private static void setProperty(SamplingStatistic statistic, String key, Object value) {
    switch(key) {
        case "transaction":
            statistic.setTransaction((String) value);
            break;
        case "sampleCount":
            statistic.setSampleCount((Long) value);
            break;
        case "errorCount":
            statistic.setErrorCount((Long) value);
            break;
        case "errorPct":
            statistic.setErrorPct((Float) value);
            break;
        case "meanResTime":
            statistic.setMeanResTime((Double) value);
            break;
        case "minResTime":
            statistic.setMinResTime((Long) value);
            break;
        case "maxResTime":
            statistic.setMaxResTime((Long) value);
            break;
        case "medianResTime":
            statistic.setMedianResTime((Double) value);
            break;
        case "pct1ResTime":
            statistic.setPct1ResTime((Double) value);
            break;
        case "pct2ResTime":
            statistic.setPct2ResTime((Double) value);
            break;
        case "pct3ResTime":
            statistic.setPct3ResTime((Double) value);
            break;
        case "throughput":
            statistic.setThroughput((Double) value);
            break;
        case "receivedKBytesPerSec":
            statistic.setReceivedKBytesPerSec((Double) value);
            break;
        case "sentKBytesPerSec":
            statistic.setSentKBytesPerSec((Double) value);
            break;
        default:
            LOGGER.warn("Unknown key: {}", key);
    }
}

