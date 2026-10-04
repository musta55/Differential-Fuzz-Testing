/**
 * Main constructor of bitMap, it should be guaranteed that the offsetLists are not null.
 *
 * @param valueOffsets The offsets to the values
 * @param numberOfRows The number of rows encoded
 */
protected ABitmap(IntArrayList[] valueOffsets, int numberOfRows) {
    int totalOffsets = 0;
    for (IntArrayList offsets : valueOffsets) {
        totalOffsets += offsets.size();
    }
    _numZeros = numberOfRows - totalOffsets;
    _offsetsLists = valueOffsets;
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

