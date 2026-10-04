public static ExecutionContext createContext(Program prog) {
    return createContext(true, DMLScript.LINEAGE, prog);
}