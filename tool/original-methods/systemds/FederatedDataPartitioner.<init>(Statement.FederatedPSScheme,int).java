public FederatedDataPartitioner(Statement.FederatedPSScheme scheme, int seed) {
    _seed = seed;
    switch(scheme) {
        case KEEP_DATA_ON_WORKER:
            _scheme = new KeepDataOnWorkerFederatedScheme();
            break;
        case SHUFFLE:
            _scheme = new ShuffleFederatedScheme();
            break;
        case REPLICATE_TO_MAX:
            _scheme = new ReplicateToMaxFederatedScheme();
            break;
        case SUBSAMPLE_TO_MIN:
            _scheme = new SubsampleToMinFederatedScheme();
            break;
        case BALANCE_TO_AVG:
            _scheme = new BalanceToAvgFederatedScheme();
            break;
        default:
            throw new DMLRuntimeException(String.format("FederatedDataPartitioner: not support data partition scheme '%s'", scheme));
    }
}