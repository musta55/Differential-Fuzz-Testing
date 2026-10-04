public UtilizationModel(final Long id, final Long workerId, final LocalDateTime timestamp, final double cpuUsage, final double memoryUsage) {
    this.id = id;
    this.workerId = workerId;
    this.timestamp = timestamp;
    this.cpuUsage = cpuUsage;
    this.memoryUsage = memoryUsage;
}