public static String displayStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("HOP DAGs recompiled (PRED, SB):\t").append(getRecompiledPredDAGs()).append("/").append(getRecompiledSBDAGs()).append(".\n");
    sb.append("HOP DAGs recompile time:\t").append(String.format("%.3f", ((double) getRecompileTime()) / 1000000000)).append(" sec.\n");
    return sb.toString();
}