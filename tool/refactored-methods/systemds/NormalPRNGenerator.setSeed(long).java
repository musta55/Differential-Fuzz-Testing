@Override
public void setSeed(long seed) {
    rnorm.setSeed(seed);
    resetPair();
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

