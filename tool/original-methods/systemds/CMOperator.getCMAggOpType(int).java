public static AggregateOperationTypes getCMAggOpType(int order) {
    if (order == 2)
        return AggregateOperationTypes.CM2;
    else if (order == 3)
        return AggregateOperationTypes.CM3;
    else if (order == 4)
        return AggregateOperationTypes.CM4;
    else if (//this is a special case to handel weighted mean
    order == 0)
        return AggregateOperationTypes.MEAN;
    else
        return AggregateOperationTypes.INVALID;
}