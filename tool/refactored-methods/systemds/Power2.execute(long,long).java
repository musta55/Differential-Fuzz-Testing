@Override
public double execute(long in1, long in2) {
    //for robustness regarding long overflows (only used for scalar instructions)
    double dval = ((double) in1 * in1);
    if (dval > Long.MAX_VALUE)
        return dval;
    //ignore in2 because always 2;
    return dval;
}