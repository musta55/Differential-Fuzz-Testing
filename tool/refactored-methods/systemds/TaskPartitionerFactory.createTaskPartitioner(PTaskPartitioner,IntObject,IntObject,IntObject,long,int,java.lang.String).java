public static TaskPartitioner createTaskPartitioner(PTaskPartitioner type, IntObject from, IntObject to, IntObject incr, long taskSize, int numThreads, String iterPredVar) {
    switch(type) {
        case FIXED:
            return createFixedTaskPartitioner(taskSize, iterPredVar, from, to, incr);
        case NAIVE:
            return createNaiveTaskPartitioner(taskSize, iterPredVar, from, to, incr);
        case STATIC:
            return createStaticTaskPartitioner(taskSize, numThreads, iterPredVar, from, to, incr);
        case FACTORING:
            return createFactoringTaskPartitioner(taskSize, numThreads, iterPredVar, from, to, incr);
        case FACTORING_CMIN:
            return createFactoringCminTaskPartitioner(taskSize, numThreads, taskSize, iterPredVar, from, to, incr);
        case FACTORING_CMAX:
            return createFactoringCmaxTaskPartitioner(taskSize, numThreads, taskSize, iterPredVar, from, to, incr);
        default:
            throw new DMLRuntimeException("Undefined task partitioner: '" + type + "'.");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private static TaskPartitioner createFixedTaskPartitioner(long taskSize, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerFixedsize(taskSize, iterPredVar, from, to, incr);
}

private static TaskPartitioner createNaiveTaskPartitioner(long taskSize, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerNaive(taskSize, iterPredVar, from, to, incr);
}

private static TaskPartitioner createStaticTaskPartitioner(long taskSize, int numThreads, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerStatic(taskSize, numThreads, iterPredVar, from, to, incr);
}

private static TaskPartitioner createFactoringTaskPartitioner(long taskSize, int numThreads, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerFactoring(taskSize, numThreads, iterPredVar, from, to, incr);
}

private static TaskPartitioner createFactoringCminTaskPartitioner(long taskSize, int numThreads, long innerTaskSize, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerFactoringCmin(taskSize, numThreads, innerTaskSize, iterPredVar, from, to, incr);
}

private static TaskPartitioner createFactoringCmaxTaskPartitioner(long taskSize, int numThreads, long innerTaskSize, String iterPredVar, IntObject from, IntObject to, IntObject incr) {
    return new TaskPartitionerFactoringCmax(taskSize, numThreads, innerTaskSize, iterPredVar, from, to, incr);
}

