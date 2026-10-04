@Override
public String toString() {
    String utilizationStr = null, trafficStr = null, eventsStr = null, dataObjectsStr = null, requestsStr = null, heavyHittersStr = null;
    if (utilization != null) {
        utilizationStr = utilization.stream().map(UtilizationModel::toString).collect(Collectors.joining(","));
    }
    if (traffic != null) {
        trafficStr = traffic.stream().map(TrafficModel::toString).collect(Collectors.joining(","));
    }
    if (events != null) {
        eventsStr = events.stream().map(EventModel::toString).collect(Collectors.joining(","));
    }
    if (dataObjects != null) {
        dataObjectsStr = dataObjects.stream().map(DataObjectModel::toString).collect(Collectors.joining(","));
    }
    if (requests != null) {
        requestsStr = requests.stream().map(RequestModel::toString).collect(Collectors.joining(","));
    }
    if (heavyHitters != null) {
        heavyHittersStr = heavyHitters.stream().map(HeavyHitterModel::toString).collect(Collectors.joining(","));
    }
    return String.format(JsonFormat, utilizationStr, trafficStr, eventsStr, dataObjectsStr, requestsStr, heavyHittersStr);
}