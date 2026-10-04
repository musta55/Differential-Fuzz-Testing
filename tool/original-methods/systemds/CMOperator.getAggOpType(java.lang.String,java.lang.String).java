public static AggregateOperationTypes getAggOpType(String fn, String order) {
    if (fn.equalsIgnoreCase("count")) {
        return AggregateOperationTypes.COUNT;
    } else if (fn.equalsIgnoreCase("sum")) {
        return AggregateOperationTypes.SUM;
    } else if (fn.equalsIgnoreCase("mean")) {
        return AggregateOperationTypes.MEAN;
    } else if (fn.equalsIgnoreCase("variance")) {
        return AggregateOperationTypes.VARIANCE;
    } else if (fn.equalsIgnoreCase("centralmoment")) {
        // in case of centralmoment, find aggIo by order
        if (order == null)
            return AggregateOperationTypes.INVALID;
        if (order.equalsIgnoreCase("2"))
            return AggregateOperationTypes.CM2;
        else if (order.equalsIgnoreCase("3"))
            return AggregateOperationTypes.CM3;
        else if (order.equalsIgnoreCase("4"))
            return AggregateOperationTypes.CM4;
        else
            return AggregateOperationTypes.INVALID;
    } else if (fn.equalsIgnoreCase("min")) {
        return AggregateOperationTypes.MIN;
    } else if (fn.equalsIgnoreCase("max")) {
        return AggregateOperationTypes.MAX;
    }
    return AggregateOperationTypes.INVALID;
}