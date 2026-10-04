public UtilizationModel(Long id, Long workerId, LocalDateTime timestamp, double cpuUsage, double memoryUsage) {
    this(new Builder().id(id).workerId(workerId).timestamp(timestamp).cpuUsage(cpuUsage).memoryUsage(memoryUsage));
}