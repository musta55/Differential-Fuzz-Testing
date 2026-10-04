@Override
public ArrayList<Integer> estimateRangeExecutors(int driverCores, long executorMemory, int executorCores) {
    // consider the cpu quota (limit) for cloud instances and
    // based on the initiated flags decides for the following methods
    // for enumeration of the number of executors:
    // 1. Increasing the number of executor with given step size (default 1)
    // 2. Exponentially increasing number of executors based on a given exponent base
    int maxAchievableLevelOfParallelism = CPU_QUOTA - driverCores;
    int currentMax = Math.min(maxExecutors, maxAchievableLevelOfParallelism / executorCores);
    ArrayList<Integer> result;
    if (expBaseExecutors > 1) {
        int maxCapacity = (int) Math.floor(Math.log(currentMax) / Math.log(2));
        result = new ArrayList<>(maxCapacity);
        int exponent = 0;
        int numExecutors;
        while ((numExecutors = (int) Math.pow(expBaseExecutors, exponent)) <= currentMax) {
            if (numExecutors >= minExecutors) {
                result.add(numExecutors);
            }
            exponent++;
        }
    } else {
        int capacity = (int) Math.floor((double) (currentMax - minExecutors + 1) / stepSizeExecutors);
        result = new ArrayList<>(capacity);
        // exclude the 0 from the iteration while keeping it as starting point to ensure predictable steps
        int numExecutors = minExecutors == 0 ? minExecutors + stepSizeExecutors : minExecutors;
        while (numExecutors <= currentMax) {
            result.add(numExecutors);
            numExecutors += stepSizeExecutors;
        }
    }
    return result;
}