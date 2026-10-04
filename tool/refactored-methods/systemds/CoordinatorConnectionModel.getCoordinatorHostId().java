public String getCoordinatorHostId() {
    return cleanCoordinatorHostId(this.coordinatorHostId);
}
// ---- helper method(s) introduced by the refactoring ----
private String cleanCoordinatorHostId(String hostId) {
    hostId = hostId.replaceFirst("/", "");
    if (hostId.contains(localhostIp)) {
        hostId = hostId.replace(localhostIp, localhostString);
    }
    hostId = hostId.replaceFirst(":\\d+", "");
    return hostId;
}

