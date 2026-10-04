public String getGroupsTypesString() {
    StringBuilder sb = new StringBuilder();
    for (String ctKey : colGroupCounts.keySet()) sb.append(ctKey + ":" + colGroupCounts.get(ctKey)[0] + " ");
    return sb.toString();
}