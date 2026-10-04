public static String displaySize() {
    //size of all cached reusable intermediates/size of reused intermediates/cache size
    StringBuilder sb = new StringBuilder();
    //in MB
    sb.append(String.format("%.3f", ((double) LineageEstimator._totReusableSize) / (1024 * 1024)));
    sb.append("/");
    //in MB
    sb.append(String.format("%.3f", ((double) LineageEstimator._totReusedSize) / (1024 * 1024)));
    sb.append("/");
    //in MB
    sb.append(String.format("%.3f", ((double) LineageEstimator.CACHE_LIMIT) / (1024 * 1024)));
    return sb.toString();
}