public static TaskPartitioner createTaskPartitioner(PTaskPartitioner type, IntObject from, IntObject to, IntObject incr, long taskSize, int numThreads, String iterPredVar) {
    switch(type) {
        case FIXED:
            return new TaskPartitionerFixedsize(taskSize, iterPredVar, from, to, incr);
        case NAIVE:
            return new TaskPartitionerNaive(taskSize, iterPredVar, from, to, incr);
        case STATIC:
            return new TaskPartitionerStatic(taskSize, numThreads, iterPredVar, from, to, incr);
        case FACTORING:
            return new TaskPartitionerFactoring(taskSize, numThreads, iterPredVar, from, to, incr);
        case FACTORING_CMIN:
            return new TaskPartitionerFactoringCmin(taskSize, numThreads, taskSize, iterPredVar, from, to, incr);
        case FACTORING_CMAX:
            return new TaskPartitionerFactoringCmax(taskSize, numThreads, taskSize, iterPredVar, from, to, incr);
        default:
            throw new DMLRuntimeException("Undefined task partitioner: '" + type + "'.");
    }
}