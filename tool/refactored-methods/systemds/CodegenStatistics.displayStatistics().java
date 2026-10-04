public static String displayStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("Codegen compile (DAG,CP,JC):\t").append(getDAGCompile()).append("/").append(getCPlanCompile()).append("/").append(getClassCompile()).append(".\n");
    sb.append("Codegen enum (ALLt/p,EVALt/p):\t").append(getEnumAll()).append("/").append(getEnumAllP()).append("/").append(getEnumEval()).append("/").append(getEnumEvalP()).append(".\n");
    sb.append("Codegen compile times (DAG,JC):\t").append(String.format("%.3f", (double) getCompileTime() / 1_000_000_000)).append("/").append(String.format("%.3f", (double) getClassCompileTime() / 1_000_000_000)).append(" sec.\n");
    sb.append("Codegen enum plan cache hits:\t").append(getPlanCacheHits()).append("/").append(getPlanCacheTotal()).append(".\n");
    sb.append("Codegen op plan cache hits:\t").append(getOpCacheHits()).append("/").append(getOpCacheTotal()).append(".\n");
    return sb.toString();
}