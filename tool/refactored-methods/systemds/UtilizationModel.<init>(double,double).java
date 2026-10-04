public UtilizationModel(double cpuUsage, double memoryUsage) {
    this(new Builder().id(-1L).workerId(-1L).timestamp(LocalDateTime.now()).cpuUsage(cpuUsage).memoryUsage(memoryUsage));
}