public String getEstimatedDistinct() {
    StringBuilder sb = new StringBuilder();
    if (compressionInfo == null)
        return "";
    sb.append("[");
    sb.append(compressionInfo.get(0).getNumVals());
    for (int i = 1; i < compressionInfo.size(); i++) sb.append(", " + compressionInfo.get(i).getNumVals());
    sb.append("]");
    return sb.toString();
}