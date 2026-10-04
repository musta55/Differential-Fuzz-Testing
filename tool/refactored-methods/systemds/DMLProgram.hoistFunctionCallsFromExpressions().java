public void hoistFunctionCallsFromExpressions() {
    try {
        hoistFunctionCallsFromFunctionStatementBlocks();
        hoistFunctionCallsFromMainProgram();
    } catch (LanguageException ex) {
        throw new RuntimeException(ex);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private Map<String, FunctionStatementBlock> collectFunctionsFromNamespace(FunctionDictionary<FunctionStatementBlock> dict) {
    Map<String, FunctionStatementBlock> functions = new HashMap<>();
    for (Entry<String, FunctionStatementBlock> e : dict.getFunctions().entrySet()) functions.put(e.getKey(), e.getValue());
    return functions;
}

private void hoistFunctionCallsFromFunctionStatementBlocks() {
    for (FunctionStatementBlock fsb : getFunctionStatementBlocks()) StatementBlock.rHoistFunctionCallsFromExpressions(fsb, this);
}

private void hoistFunctionCallsFromMainProgram() {
    List<StatementBlock> tmp = new ArrayList<>();
    for (StatementBlock sb : _blocks) tmp.addAll(StatementBlock.rHoistFunctionCallsFromExpressions(sb, this));
    _blocks = new ArrayList<>(tmp);
}

private void appendNamespaceDetails(StringBuilder sb, String namespaceKey) {
    sb.append("NAMESPACE = ").append(namespaceKey).append("\n");
    FunctionDictionary<FunctionStatementBlock> dict = getNamespaces().get(namespaceKey);
    sb.append("FUNCTIONS = ");
    for (FunctionStatementBlock fsb : dict.getFunctions().values()) {
        sb.append(fsb).append(", ");
    }
    sb.append("\n").append("********************************** \n");
}

