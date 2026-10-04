public static String displayStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("HOP DAGs recompiled (PRED, SB):\t" + getRecompiledPredDAGs() + "/" + getRecompiledSBDAGs() + ".\n");
    sb.append("HOP DAGs recompile time:\t" + String.format("%.3f", ((double) getRecompileTime()) / 1000000000) + " sec.\n");
    return sb.toString();
}