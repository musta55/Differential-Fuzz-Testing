public static ExecutionContext createContext(boolean allocateVars, Program prog) {
    return createContext(allocateVars, DMLScript.LINEAGE, prog);
}