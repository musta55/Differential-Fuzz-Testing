/**
 * Get the sum of offsets contained.
 *
 * @return The sum of offsets
 */
public final long getNumOffsets() {
    long totalOffsets = 0;
    for (IntArrayList offsets : _offsetsLists) {
        totalOffsets += offsets.size();
    }
    return totalOffsets;
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

