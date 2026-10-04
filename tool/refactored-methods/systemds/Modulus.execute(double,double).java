@Override
public double execute(double in1, double in2) {
    if (isZero(in2))
        return Double.NaN;
    return in1 - _intdiv.execute(in1, in2) * in2;
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isZero(double value) {
    return value == 0.0 || value == -0.0;
}

private boolean isZero(long value) {
    return value == 0;
}

