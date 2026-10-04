@Override
public double nextDouble() {
    double d = flag ? getSecondValueAndComputeNextPair() : getFirstValue();
    toggleFlag();
    return d;
}
// ---- helper method(s) introduced by the refactoring ----
private void resetPair() {
    pair = new RandNPair();
    flag = false;
    pair.compute(rnorm);
}

private double getFirstValue() {
    return pair.getFirst();
}

private double getSecondValueAndComputeNextPair() {
    double value = pair.getSecond();
    pair.compute(rnorm);
    return value;
}

private void toggleFlag() {
    flag = !flag;
}

