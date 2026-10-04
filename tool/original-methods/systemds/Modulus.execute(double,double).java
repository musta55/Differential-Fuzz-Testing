@Override
public double execute(double in1, double in2) {
    if (in2 == 0.0 || in2 == -0.0)
        return Double.NaN;
    return in1 - _intdiv.execute(in1, in2) * in2;
}