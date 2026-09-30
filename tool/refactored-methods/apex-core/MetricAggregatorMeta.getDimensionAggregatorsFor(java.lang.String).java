public String[] getDimensionAggregatorsFor(String logicalMetricName) {
    if (dimensionsScheme == null) {
        return new String[0];
    }
    return dimensionsScheme.getDimensionAggregationsFor(logicalMetricName);
}