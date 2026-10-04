public static String displayComputeTime() {
    StringBuilder sb = new StringBuilder();
    //in sec
    sb.append(String.format("%.3f", Statistics.getRunTime() * 1e-9));
    sb.append("/");
    //in sec
    sb.append(String.format("%.3f", ((double) _ctimeSaved.longValue()) / 1000000000));
    return sb.toString();
}