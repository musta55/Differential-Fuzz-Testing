public String getEstimatedDistinct() {
    return buildValuesString(compressionInfo, CompressedSizeInfoColGroup::getNumVals);
}
// ---- helper method(s) introduced by the refactoring ----
private String buildValuesString(List<CompressedSizeInfoColGroup> groups, java.util.function.Function<CompressedSizeInfoColGroup, Integer> valueExtractor) {
    StringBuilder sb = new StringBuilder();
    if (groups == null || groups.isEmpty())
        return "";
    sb.append("[");
    sb.append(valueExtractor.apply(groups.get(0)));
    for (int i = 1; i < groups.size(); i++) sb.append(", " + valueExtractor.apply(groups.get(i)));
    sb.append("]");
    return sb.toString();
}

