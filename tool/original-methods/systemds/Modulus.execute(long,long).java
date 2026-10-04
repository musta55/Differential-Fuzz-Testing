@Override
public double execute(long in1, long in2) {
    if (in2 == 0)
        return Double.NaN;
    return in1 - _intdiv.execute(in1, in2) * in2;
}