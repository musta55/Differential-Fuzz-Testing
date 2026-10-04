public FederatedDataPartitioner(Statement.FederatedPSScheme scheme, int seed) {
    _seed = seed;
    _scheme = createScheme(scheme);
}
// ---- helper method(s) introduced by the refactoring ----
private DataPartitionFederatedScheme createScheme(Statement.FederatedPSScheme scheme) {
    switch(scheme) {
        case KEEP_DATA_ON_WORKER:
            return new KeepDataOnWorkerFederatedScheme();
        case SHUFFLE:
            return new ShuffleFederatedScheme();
        case REPLICATE_TO_MAX:
            return new ReplicateToMaxFederatedScheme();
        case SUBSAMPLE_TO_MIN:
            return new SubsampleToMinFederatedScheme();
        case BALANCE_TO_AVG:
            return new BalanceToAvgFederatedScheme();
        default:
            throw new DMLRuntimeException(String.format("FederatedDataPartitioner: not support data partition scheme '%s'", scheme));
    }
}

