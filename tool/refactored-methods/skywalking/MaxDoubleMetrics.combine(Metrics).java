@Override
public final boolean combine(Metrics metrics) {
    MaxDoubleMetrics maxDoubleMetrics = (MaxDoubleMetrics) metrics;
    combineWithDouble(maxDoubleMetrics.value);
    return true;
}
// ---- helper method(s) introduced by the refactoring ----
@Entrance
public final void combineWithDouble(@SourceFrom double count) {
    if (count > this.value) {
        this.value = count;
    }
}

