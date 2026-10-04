public UtilizationModel(final Long workerId, final double cpuUsage, final double memoryUsage) {
    this(-1L, workerId, LocalDateTime.now(), cpuUsage, memoryUsage);
}