public static AggregateOperationTypes getCMAggOpType(int order) {
    switch(order) {
        case 2:
            return AggregateOperationTypes.CM2;
        case 3:
            return AggregateOperationTypes.CM3;
        case 4:
            return AggregateOperationTypes.CM4;
        case //this is a special case to handle weighted mean
        0:
            return AggregateOperationTypes.MEAN;
        default:
            return AggregateOperationTypes.INVALID;
    }
}