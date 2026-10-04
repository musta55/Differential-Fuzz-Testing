public static String displayStatistics() {
    StringBuilder sb = new StringBuilder();
    sb.append("Codegen compile (DAG,CP,JC):\t" + getDAGCompile() + "/" + getCPlanCompile() + "/" + getClassCompile() + ".\n");
    sb.append("Codegen enum (ALLt/p,EVALt/p):\t" + getEnumAll() + "/" + getEnumAllP() + "/" + getEnumEval() + "/" + getEnumEvalP() + ".\n");
    sb.append("Codegen compile times (DAG,JC):\t" + String.format("%.3f", (double) getCompileTime() / 1000000000) + "/" + String.format("%.3f", (double) getClassCompileTime() / 1000000000) + " sec.\n");
    sb.append("Codegen enum plan cache hits:\t" + getPlanCacheHits() + "/" + getPlanCacheTotal() + ".\n");
    sb.append("Codegen op plan cache hits:\t" + getOpCacheHits() + "/" + getOpCacheTotal() + ".\n");
    return sb.toString();
}