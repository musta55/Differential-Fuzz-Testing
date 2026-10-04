@Override
public void dispatch(Process source) {
    final ProcessTraffic traffic = new ProcessTraffic();
    traffic.setServiceId(source.getServiceId());
    traffic.setInstanceId(source.getInstanceId());
    traffic.setName(source.getName());
    if (CollectionUtils.isNotEmpty(source.getLabels())) {
        traffic.setLabelsJson(GSON.toJson(source.getLabels()));
    } else {
        traffic.setLabelsJson(Const.EMPTY_STRING);
    }
    traffic.setAgentId(source.getAgentId());
    traffic.setProperties(source.getProperties());
    if (source.getProfilingSupportStatus() != null) {
        traffic.setProfilingSupportStatus(source.getProfilingSupportStatus().value());
    }
    if (source.getDetectType() != null) {
        traffic.setDetectType(source.getDetectType().value());
    }
    traffic.setTimeBucket(source.getTimeBucket());
    traffic.setLastPingTimestamp(source.getTimeBucket());
    MetricsStreamProcessor.getInstance().in(traffic);
}