@Override
public ProfileTaskCommand deserialize(Command command) {
    final List<KeyStringValuePair> argsList = command.getArgsList();
    String serialNumber = null;
    String taskId = null;
    String endpointName = null;
    int duration = 0;
    int minDurationThreshold = 0;
    int dumpPeriod = 0;
    int maxSamplingCount = 0;
    long startTime = 0;
    long createTime = 0;
    for (final KeyStringValuePair pair : argsList) {
        if ("SerialNumber".equals(pair.getKey())) {
            serialNumber = pair.getValue();
        } else if ("EndpointName".equals(pair.getKey())) {
            endpointName = pair.getValue();
        } else if ("TaskId".equals(pair.getKey())) {
            taskId = pair.getValue();
        } else if ("Duration".equals(pair.getKey())) {
            duration = Integer.parseInt(pair.getValue());
        } else if ("MinDurationThreshold".equals(pair.getKey())) {
            minDurationThreshold = Integer.parseInt(pair.getValue());
        } else if ("DumpPeriod".equals(pair.getKey())) {
            dumpPeriod = Integer.parseInt(pair.getValue());
        } else if ("MaxSamplingCount".equals(pair.getKey())) {
            maxSamplingCount = Integer.parseInt(pair.getValue());
        } else if ("StartTime".equals(pair.getKey())) {
            startTime = Long.parseLong(pair.getValue());
        } else if ("CreateTime".equals(pair.getKey())) {
            createTime = Long.parseLong(pair.getValue());
        }
    }
    return new ProfileTaskCommand(serialNumber, taskId, endpointName, duration, minDurationThreshold, dumpPeriod, maxSamplingCount, startTime, createTime);
}