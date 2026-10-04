public static AggregateOperationTypes getAggOpType(String fn, String order) {
    switch(fn.toLowerCase()) {
        case "count":
            return AggregateOperationTypes.COUNT;
        case "sum":
            return AggregateOperationTypes.SUM;
        case "mean":
            return AggregateOperationTypes.MEAN;
        case "variance":
            return AggregateOperationTypes.VARIANCE;
        case "centralmoment":
            if (order == null) {
                return AggregateOperationTypes.INVALID;
            }
            switch(order.toLowerCase()) {
                case "2":
                    return AggregateOperationTypes.CM2;
                case "3":
                    return AggregateOperationTypes.CM3;
                case "4":
                    return AggregateOperationTypes.CM4;
                default:
                    return AggregateOperationTypes.INVALID;
            }
        case "min":
            return AggregateOperationTypes.MIN;
        case "max":
            return AggregateOperationTypes.MAX;
        default:
            return AggregateOperationTypes.INVALID;
    }
}