@Override
public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("\nCompressionStatistics:");
    appendStat(sb, "Dense Size", denseSize);
    appendStat(sb, "Original Size", originalSize);
    appendStat(sb, "Compressed Size", compressedSize);
    appendStat(sb, "CompressionRatio", getRatio());
    appendStat(sb, "DenseCompressionRatio", getDenseRatio());
    if (colGroupCounts != null) {
        appendStat(sb, "CompressionTypes", getGroupsTypesString());
        appendStat(sb, "CompressionGroupSizes", getGroupsSizesString());
    }
    return sb.toString();
}
// ---- helper method(s) introduced by the refactoring ----
private void updateColGroupCount(AColGroup colGroup) {
    String colGroupName = colGroup.getClass().getSimpleName();
    int colCount = colGroup.getNumCols();
    colGroupCounts.merge(colGroupName, new int[] { 1, colCount }, this::sumCounts);
}

private int[] sumCounts(int[] a, int[] b) {
    return new int[] { a[0] + b[0], a[1] + b[1] };
}

private void appendGroupCounts(StringBuilder sb, int index) {
    for (String ctKey : colGroupCounts.keySet()) {
        sb.append(ctKey).append(":").append(colGroupCounts.get(ctKey)[index]).append(" ");
    }
}

private void appendStat(StringBuilder sb, String label, Object value) {
    sb.append("\n").append(label).append(" : ").append(value);
}

