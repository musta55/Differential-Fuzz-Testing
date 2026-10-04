/**
 * Get a specific offset list.
 *
 * @param index The index to look at inside the contained array
 * @return the Offset list at the index
 */
public final IntArrayList getOffsetsList(int index) {
    return _offsetsLists[index];
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

