public UtilizationModel(final double cpuUsage, final double memoryUsage) {
    this(-1L, -1L, LocalDateTime.now(), cpuUsage, memoryUsage);
}