@Override
public ArrayList<Integer> estimateRangeExecutors(int driverCores, long executorMemory, int executorCores) {
    int maxAchievableLevelOfParallelism = calculateMaxAchievableLevelOfParallelism(driverCores, executorCores);
    int currentMax = Math.min(maxExecutors, maxAchievableLevelOfParallelism);
    return expBaseExecutors > 1 ? generateExponentialExecutorRange(currentMax) : generateLinearExecutorRange(currentMax);
}
// ---- helper method(s) introduced by the refactoring ----
private int calculateMaxAchievableLevelOfParallelism(int driverCores, int executorCores) {
    return (CPU_QUOTA - driverCores) / executorCores;
}

private ArrayList<Integer> generateExponentialExecutorRange(int currentMax) {
    int maxCapacity = (int) Math.floor(Math.log(currentMax) / Math.log(expBaseExecutors));
    ArrayList<Integer> result = new ArrayList<>(maxCapacity);
    for (int exponent = 0; ; exponent++) {
        int numExecutors = (int) Math.pow(expBaseExecutors, exponent);
        if (numExecutors > currentMax)
            break;
        if (numExecutors >= minExecutors) {
            result.add(numExecutors);
        }
    }
    return result;
}

private ArrayList<Integer> generateLinearExecutorRange(int currentMax) {
    int capacity = (int) Math.floor((double) (currentMax - minExecutors + 1) / stepSizeExecutors);
    ArrayList<Integer> result = new ArrayList<>(capacity);
    for (int numExecutors = minExecutors == 0 ? minExecutors + stepSizeExecutors : minExecutors; numExecutors <= currentMax; numExecutors += stepSizeExecutors) {
        result.add(numExecutors);
    }
    return result;
}

