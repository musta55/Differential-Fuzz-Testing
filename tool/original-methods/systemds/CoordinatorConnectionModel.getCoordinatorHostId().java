public String getCoordinatorHostId() {
    this.coordinatorHostId = this.coordinatorHostId.replaceFirst("/", "");
    if (this.coordinatorHostId.contains(localhostIp)) {
        this.coordinatorHostId = this.coordinatorHostId.replace(localhostIp, localhostString);
    }
    this.coordinatorHostId = this.coordinatorHostId.replaceFirst(":\\d+", "");
    return this.coordinatorHostId;
}