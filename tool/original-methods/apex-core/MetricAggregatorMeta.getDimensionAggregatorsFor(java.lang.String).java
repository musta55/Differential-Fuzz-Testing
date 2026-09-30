public String[] getDimensionAggregatorsFor(String logicalMetricName) {
    if (dimensionsScheme == null) {
        return null;
    }
    return dimensionsScheme.getDimensionAggregationsFor(logicalMetricName);
}