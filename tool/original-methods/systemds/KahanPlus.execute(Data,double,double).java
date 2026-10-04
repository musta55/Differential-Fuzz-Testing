// in1, in2 is the sum, in3 is the correction
@Override
public Data execute(Data in1, double in2, double in3) {
    KahanObject kahanObj = (KahanObject) in1;
    //fast path for INF/-INF in order to ensure result correctness
    //(computing corrections otherwise incorrectly computes NaN)
    if (Double.isInfinite(kahanObj._sum) || Double.isInfinite(in2)) {
        kahanObj.set(Double.isInfinite(in2) ? in2 : kahanObj._sum, 0);
        return kahanObj;
    }
    //default path for any other value
    double correction = in2 + (kahanObj._correction + in3);
    double sum = kahanObj._sum + correction;
    //prevent eager JIT opt
    kahanObj.set(sum, correction - (sum - kahanObj._sum));
    return kahanObj;
}