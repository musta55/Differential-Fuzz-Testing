@Override
public void setValue(Object value) {
    if (!(value instanceof Double)) {
        setText("#N/A");
        return;
    }
    double rate = (Double) value;
    if (Double.compare(rate, Double.MAX_VALUE) == 0) {
        setText("#N/A");
        return;
    }
    UnitInfo unitInfo = determineUnit(rate);
    setText(formatter.format(unitInfo.rate) + "/" + unitInfo.unit);
}
// ---- helper method(s) introduced by the refactoring ----
private static UnitInfo determineUnit(double rate) {
    String unit = "sec";
    if (rate < 1.0) {
        rate *= 60.0;
        unit = "min";
    }
    if (rate < 1.0) {
        rate *= 60.0;
        unit = "hour";
    }
    return new UnitInfo(rate, unit);
}

