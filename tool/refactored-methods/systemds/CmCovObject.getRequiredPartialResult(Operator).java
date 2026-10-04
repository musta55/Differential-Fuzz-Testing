public double getRequiredPartialResult(Operator op) {
    if (op instanceof CMOperator) {
        AggregateOperationTypes agg = ((CMOperator) op).aggOpType;
        switch(agg) {
            case COUNT:
                return 0;
            case MEAN:
                return getMeanResult();
            case CM2:
            case CM3:
            case CM4:
            case VARIANCE:
                throw new DMLRuntimeException("Aggregation operator '" + agg.toString() + "' does not apply to partial aggregation.");
            default:
                throw new DMLRuntimeException("Invalid aggregate in CM_CV_Object: " + agg);
        }
    } else
        return c2._sum;
}
// ---- helper method(s) introduced by the refactoring ----
private double getCountResult() {
    return w;
}

private double getMeanResult() {
    return mean._sum;
}

private double getCm2Result() {
    return m2._sum / w;
}

private double getCm3Result() {
    return m3._sum / w;
}

private double getCm4Result() {
    return m4._sum / w;
}

private double getMinResult() {
    return min;
}

private double getMaxResult() {
    return max;
}

private double getVarianceResult() {
    return w == 1.0 ? 0 : m2._sum / (w - 1);
}

