public double getRequiredPartialResult(Operator op) {
    if (op instanceof CMOperator) {
        AggregateOperationTypes agg = ((CMOperator) op).aggOpType;
        switch(agg) {
            case COUNT:
                return 0;
            case MEAN:
                return mean._sum;
            case CM2:
            case CM3:
            case CM4:
            case VARIANCE:
                throw new DMLRuntimeException("Aggregation operator '" + agg.toString() + "' does not apply to partial aggregation.");
            default:
                throw new DMLRuntimeException("Invalid aggreagte in CM_CV_Object: " + agg);
        }
    } else
        return c2._sum;
}