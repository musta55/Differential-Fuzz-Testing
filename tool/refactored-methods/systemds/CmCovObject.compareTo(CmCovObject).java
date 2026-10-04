public int compareTo(CmCovObject that) {
    int result = Double.compare(w, that.w);
    if (result != 0)
        return result;
    result = KahanObject.compare(mean, that.mean);
    if (result != 0)
        return result;
    result = KahanObject.compare(m2, that.m2);
    if (result != 0)
        return result;
    result = KahanObject.compare(m3, that.m3);
    if (result != 0)
        return result;
    result = KahanObject.compare(m4, that.m4);
    if (result != 0)
        return result;
    result = KahanObject.compare(mean_v, that.mean_v);
    if (result != 0)
        return result;
    result = Double.compare(min, that.min);
    if (result != 0)
        return result;
    result = Double.compare(max, that.max);
    if (result != 0)
        return result;
    return KahanObject.compare(c2, that.c2);
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

