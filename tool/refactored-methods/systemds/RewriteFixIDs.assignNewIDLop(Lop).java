private void assignNewIDLop(Lop lop) {
    if (lop.isVisited()) {
        return;
    }
    if (lop.getInputs().isEmpty()) {
        // leaf node
        lop.setNewID();
        lop.setVisited();
        return;
    }
    for (Lop input : lop.getInputs()) {
        assignNewIDLop(input);
    }
    lop.setNewID();
    lop.setVisited();
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldSkipRewrite() {
    return !ConfigurationManager.isPrefetchEnabled() && !ConfigurationManager.isBroadcastEnabled() && !ConfigurationManager.isCheckpointEnabled();
}

private StatementBlock getInnerStatementBlock(StatementBlock sb) {
    return sb instanceof WhileStatementBlock ? ((WhileStatement) sb.getStatement(0)).getBody().get(0) : ((ForStatement) sb.getStatement(0)).getBody().get(0);
}

