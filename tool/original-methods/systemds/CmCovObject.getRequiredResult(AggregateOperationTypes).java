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
            return w;
        case MEAN:
            return mean._sum;
        case CM2:
            return m2._sum / w;
        case CM3:
            return m3._sum / w;
        case CM4:
            return m4._sum / w;
        case MIN:
            return min;
        case MAX:
            return max;
        case VARIANCE:
            return w == 1.0 ? 0 : m2._sum / (w - 1);
        default:
            throw new DMLRuntimeException("Invalid aggreagte in CM_CV_Object: " + agg);
    }
}