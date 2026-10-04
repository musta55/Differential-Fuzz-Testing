/**
 * Simplified version of execute(Data in1, double in2)
 * without exception handling and casts.
 *
 * @param in1 kahan object input
 * @param in2 double input
 */
@Override
public void execute2(KahanObject in1, double in2) {
    updateKahanObject(in1, in2, 0);
}
// ---- helper method(s) introduced by the refactoring ----
private void updateKahanObject(KahanObject kahanObj, double in2, double correctionOffset) {
    //fast path for INF/-INF in order to ensure result correctness
    //(computing corrections otherwise incorrectly computes NaN)
    if (Double.isInfinite(kahanObj._sum) || Double.isInfinite(in2)) {
        kahanObj.set(Double.isInfinite(in2) ? in2 : kahanObj._sum, 0);
        return;
    }
    //default path for any other value
    double correction = in2 + (kahanObj._correction + correctionOffset);
    double sum = kahanObj._sum + correction;
    //prevent eager JIT opt
    kahanObj.set(sum, correction - (sum - kahanObj._sum));
}

