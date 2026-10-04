public String getNrColumnsString() {
    StringBuilder sb = new StringBuilder();
    if (compressionInfo == null)
        return "";
    sb.append("[");
    sb.append(compressionInfo.get(0).getColumns().size());
    for (int i = 1; i < compressionInfo.size(); i++) sb.append(", " + compressionInfo.get(i).getColumns().size());
    sb.append("]");
    return sb.toString();
}