private UtilizationModel(Builder builder) {
    this.id = builder.id;
    this.workerId = builder.workerId;
    this.timestamp = builder.timestamp;
    this.cpuUsage = builder.cpuUsage;
    this.memoryUsage = builder.memoryUsage;
}