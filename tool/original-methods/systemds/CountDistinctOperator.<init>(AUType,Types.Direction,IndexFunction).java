public CountDistinctOperator(AUType opType, Types.Direction direction, IndexFunction indexFunction) {
    super(new AggregateOperator(0, Plus.getPlusFnObject()), indexFunction, direction, 1);
    switch(opType) {
        case COUNT_DISTINCT:
            this.operatorType = CountDistinctOperatorTypes.COUNT;
            break;
        case COUNT_DISTINCT_APPROX:
            this.operatorType = CountDistinctOperatorTypes.KMV;
            break;
        default:
            throw new DMLRuntimeException(opType + " not supported for CountDistinct Operator");
    }
    this.hashType = HashType.LinearHash;
}