/**
 * Get the number of offsets for a specific unique offset.
 *
 * @param index The offset index.
 * @return The number of offsets for this unique value.
 */
public final int getNumOffsets(int index) {
    return _offsetsLists[index].size();
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

