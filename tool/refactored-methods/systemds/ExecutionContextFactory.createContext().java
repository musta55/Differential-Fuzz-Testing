public static ExecutionContext createContext() {
    return createContext(true, DMLScript.LINEAGE, null, DMLScript.getGlobalExecMode());
}