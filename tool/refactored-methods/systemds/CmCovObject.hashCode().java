@Override
public int hashCode() {
    throw new UnsupportedOperationException("hashCode() should never be called on instances of this class.");
}
// ---- helper method(s) introduced by the refactoring ----
private double getCountResult() {
    return w;
}

private double getMeanResult() {
    return mean._sum;
}

private double getCm2Result() {
    return m2._sum / w;
}

private double getCm3Result() {
    return m3._sum / w;
}

private double getCm4Result() {
    return m4._sum / w;
}

private double getMinResult() {
    return min;
}

private double getMaxResult() {
    return max;
}

private double getVarianceResult() {
    return w == 1.0 ? 0 : m2._sum / (w - 1);
}

