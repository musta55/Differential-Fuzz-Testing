@Override
public String toString() {
    StringBuilder stringBuilder = new StringBuilder();
    appendClassName(stringBuilder);
    appendZeroCount(stringBuilder);
    appendOffsets(stringBuilder);
    addToString(stringBuilder);
    return stringBuilder.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void appendClassName(StringBuilder stringBuilder) {
    stringBuilder.append(getClass().getSimpleName());
    stringBuilder.append("  ");
}

private void appendZeroCount(StringBuilder stringBuilder) {
    stringBuilder.append("zeros:  ");
    stringBuilder.append(_numZeros);
    stringBuilder.append("\n");
}

private void appendOffsets(StringBuilder stringBuilder) {
    stringBuilder.append("Offsets:");
    stringBuilder.append(Arrays.toString(_offsetsLists));
    stringBuilder.append("\n");
}

