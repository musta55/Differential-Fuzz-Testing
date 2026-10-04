/**
 * Return the result of the aggregated operation given the
 * operation type.
 *
 * @param agg aggregate operation type
 * @return result of the aggregated operation given the operation type
 */
public double getRequiredResult(AggregateOperationTypes agg) {
    switch(agg) {
        case COUNT:
            return getCountResult();
        case MEAN:
            return getMeanResult();
        case CM2:
            return getCm2Result();
        case CM3:
            return getCm3Result();
        case CM4:
            return getCm4Result();
        case MIN:
            return getMinResult();
        case MAX:
            return getMaxResult();
        case VARIANCE:
            return getVarianceResult();
        default:
            throw new DMLRuntimeException("Invalid aggregate in CM_CV_Object: " + agg);
    }
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

