/**
 * Simplified version of execute(Data in1, double in2)
 * without exception handling and casts.
 *
 * @param in1 kahan object input
 * @param in2 double input
 */
@Override
public void execute2(KahanObject in1, double in2) {
    //fast path for INF/-INF in order to ensure result correctness
    //(computing corrections otherwise incorrectly computes NaN)
    if (Double.isInfinite(in1._sum) || Double.isInfinite(in2)) {
        in1.set(Double.isInfinite(in2) ? in2 : in1._sum, 0);
        return;
    }
    //default path for any other value
    double correction = in2 + in1._correction;
    double sum = in1._sum + correction;
    //prevent eager JIT opt
    in1.set(sum, correction - (sum - in1._sum));
}