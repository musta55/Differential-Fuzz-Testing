@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    appendIfGreaterThanZero(sb, "Sca", scans);
    appendIfGreaterThanZero(sb, "DeC", decompressions);
    appendIfGreaterThanZero(sb, "OvD", overlappingDecompressions);
    appendIfGreaterThanZero(sb, "LMM", leftMultiplications);
    appendIfGreaterThanZero(sb, "RMM", rightMultiplications);
    appendIfGreaterThanZero(sb, "CMM", compressedMultiplications);
    appendIfGreaterThanZero(sb, "dic", dictionaryOps);
    appendIfGreaterThanZero(sb, "ind", indexing);
    if (sb.length() > 0)
        // remove last semicolon
        sb.setLength(sb.length() - 1);
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void appendIfGreaterThanZero(StringBuilder sb, String label, int value) {
    if (value > 0)
        sb.append(String.format("%s:%d;", label, value));
}

