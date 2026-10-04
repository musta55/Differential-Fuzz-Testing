/**
 * This method is called in {@link MeterSystem#create} process through dynamic Java codes.
 *
 * @param metricName metric name
 * @param scopeId    scope Id defined in {@link DefaultScopeDefine}
 */
public void initMeta(String metricName, int scopeId) {
    this.metadata.setMetricsName(metricName);
    this.metadata.setScope(scopeId);
}