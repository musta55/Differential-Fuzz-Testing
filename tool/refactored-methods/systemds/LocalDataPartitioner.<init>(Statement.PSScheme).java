/**
 * Constructs a LocalDataPartitioner with the specified data partition scheme.
 *
 * @param scheme the data partition scheme to use
 */
public LocalDataPartitioner(Statement.PSScheme scheme) {
    _scheme = createDataPartitionScheme(scheme);
}
// ---- helper method(s) introduced by the refactoring ----
private DataPartitionLocalScheme createDataPartitionScheme(Statement.PSScheme scheme) {
    switch(scheme) {
        case DISJOINT_CONTIGUOUS:
            return new DCLocalScheme();
        case DISJOINT_ROUND_ROBIN:
            return new DRRLocalScheme();
        case DISJOINT_RANDOM:
            return new DRLocalScheme();
        case OVERLAP_RESHUFFLE:
            return new ORLocalScheme();
        default:
            throw new DMLRuntimeException(String.format("LocalDataPartitioner: not support data partition scheme '%s'", scheme));
    }
}

