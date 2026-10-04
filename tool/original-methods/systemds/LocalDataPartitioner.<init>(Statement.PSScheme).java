public LocalDataPartitioner(Statement.PSScheme scheme) {
    switch(scheme) {
        case DISJOINT_CONTIGUOUS:
            _scheme = new DCLocalScheme();
            break;
        case DISJOINT_ROUND_ROBIN:
            _scheme = new DRRLocalScheme();
            break;
        case DISJOINT_RANDOM:
            _scheme = new DRLocalScheme();
            break;
        case OVERLAP_RESHUFFLE:
            _scheme = new ORLocalScheme();
            break;
        default:
            throw new DMLRuntimeException(String.format("LocalDataPartitioner: not support data partition scheme '%s'", scheme));
    }
}