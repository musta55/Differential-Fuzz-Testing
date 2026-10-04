public String getGroupsSizesString() {
    StringBuilder sb = new StringBuilder();
    for (String ctKey : colGroupCounts.keySet()) sb.append(ctKey + ":" + colGroupCounts.get(ctKey)[1] + " ");
    return sb.toString();
}