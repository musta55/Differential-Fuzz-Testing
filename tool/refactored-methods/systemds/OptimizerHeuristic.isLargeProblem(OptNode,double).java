////////////////////////////////////////////////////
// Overwritten rewrites (see rulebased optimizer) //
////////////////////////////////////////////////////
/**
 * Used as one condition in rewriteSetExecutionStategy in order to decide if
 * Spark execution makes sense if all the other constraints are given.
 */
@Override
protected boolean isLargeProblem(OptNode pn, double M) {
    try {
        double T = _cost.getEstimate(TestMeasure.EXEC_TIME, pn);
        return T >= EXEC_TIME_THRESHOLD && M > PROB_SIZE_THRESHOLD_MB;
    } catch (DMLRuntimeException e) {
        LOG.error("Failed to estimate execution time.", e);
    }
    return false;
}