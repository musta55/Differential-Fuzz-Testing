public Map<String, FunctionStatementBlock> getNamedNSFunctionStatementBlocks() {
    Map<String, FunctionStatementBlock> ret = new HashMap<>();
    for (FunctionDictionary<FunctionStatementBlock> dict : _namespaces.values()) for (Entry<String, FunctionStatementBlock> e : dict.getFunctions().entrySet()) ret.put(e.getKey(), e.getValue());
    return ret;
}