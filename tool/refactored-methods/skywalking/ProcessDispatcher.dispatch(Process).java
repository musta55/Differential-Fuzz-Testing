@Override
public void dispatch(Process source) {
    final ProcessTraffic traffic = new ProcessTraffic();
    setBasicProperties(traffic, source);
    setLabels(traffic, source);
    setAdvancedProperties(traffic, source);
    setTimestamps(traffic, source);
    MetricsStreamProcessor.getInstance().in(traffic);
}
// ---- helper method(s) introduced by the refactoring ----
private void setBasicProperties(ProcessTraffic traffic, Process source) {
    traffic.setServiceId(source.getServiceId());
    traffic.setInstanceId(source.getInstanceId());
    traffic.setName(source.getName());
}

private void setLabels(ProcessTraffic traffic, Process source) {
    if (CollectionUtils.isNotEmpty(source.getLabels())) {
        traffic.setLabelsJson(GSON.toJson(source.getLabels()));
    } else {
        traffic.setLabelsJson(Const.EMPTY_STRING);
    }
}

private void setAdvancedProperties(ProcessTraffic traffic, Process source) {
    traffic.setAgentId(source.getAgentId());
    traffic.setProperties(source.getProperties());
    if (source.getProfilingSupportStatus() != null) {
        traffic.setProfilingSupportStatus(source.getProfilingSupportStatus().value());
    }
    if (source.getDetectType() != null) {
        traffic.setDetectType(source.getDetectType().value());
    }
}

private void setTimestamps(ProcessTraffic traffic, Process source) {
    traffic.setTimeBucket(source.getTimeBucket());
    traffic.setLastPingTimestamp(source.getTimeBucket());
}

