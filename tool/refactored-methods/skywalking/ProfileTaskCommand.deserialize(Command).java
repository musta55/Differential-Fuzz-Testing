@Override
public ProfileTaskCommand deserialize(Command command) {
    final List<KeyStringValuePair> argsList = command.getArgsList();
    Map<String, String> argsMap = new HashMap<>();
    for (final KeyStringValuePair pair : argsList) {
        argsMap.put(pair.getKey(), pair.getValue());
    }
    String serialNumber = argsMap.getOrDefault("SerialNumber", "");
    String taskId = argsMap.getOrDefault("TaskId", "");
    String endpointName = argsMap.getOrDefault("EndpointName", "");
    int duration = Integer.parseInt(argsMap.getOrDefault("Duration", "0"));
    int minDurationThreshold = Integer.parseInt(argsMap.getOrDefault("MinDurationThreshold", "0"));
    int dumpPeriod = Integer.parseInt(argsMap.getOrDefault("DumpPeriod", "0"));
    int maxSamplingCount = Integer.parseInt(argsMap.getOrDefault("MaxSamplingCount", "0"));
    long startTime = Long.parseLong(argsMap.getOrDefault("StartTime", "0"));
    long createTime = Long.parseLong(argsMap.getOrDefault("CreateTime", "0"));
    return new ProfileTaskCommand(serialNumber, taskId, endpointName, duration, minDurationThreshold, dumpPeriod, maxSamplingCount, startTime, createTime);
}