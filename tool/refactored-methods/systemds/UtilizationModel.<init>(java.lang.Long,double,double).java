public UtilizationModel(Long workerId, double cpuUsage, double memoryUsage) {
    this(new Builder().id(-1L).workerId(workerId).timestamp(LocalDateTime.now()).cpuUsage(cpuUsage).memoryUsage(memoryUsage));
}