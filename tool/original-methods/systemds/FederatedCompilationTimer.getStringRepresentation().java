public static String getStringRepresentation() {
    if (activated && timesNotNull()) {
        long totalFetchTime = getTotalFetchTime();
        long privPropagationTime = privProcessTime.getDuration() - totalFetchTime;
        long basicCompileTime = getBasicCompileTime();
        StringBuilder sb = new StringBuilder();
        sb.append("Basic Compilation Time:\t\t").append(nanoToSeconds(basicCompileTime)).append("\n");
        sb.append("Total Privacy Fetch Time:\t").append(nanoToSeconds(totalFetchTime)).append("\n");
        sb.append("Privacy Propagation Time:\t").append(nanoToSeconds(privPropagationTime)).append("\n");
        sb.append("Plan Enumeration Time:\t\t").append(nanoToSeconds(enumerationTime.getDuration())).append("\n");
        sb.append("Plan Selection Time:\t\t").append(nanoToSeconds(selectPlanTime.getDuration())).append("\n");
        return sb.toString();
    } else
        return "";
}