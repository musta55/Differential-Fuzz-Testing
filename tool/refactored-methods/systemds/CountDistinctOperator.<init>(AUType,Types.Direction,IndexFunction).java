public CountDistinctOperator(AUType opType, Types.Direction direction, IndexFunction indexFunction) {
    this(opType == AUType.COUNT_DISTINCT ? CountDistinctOperatorTypes.COUNT : CountDistinctOperatorTypes.KMV, direction, indexFunction, HashType.LinearHash);
}