private void assignNewIDStatementBlock(StatementBlock sb) {
    if (sb.getLops() != null && !sb.getLops().isEmpty()) {
        for (Lop root : sb.getLops()) {
            assignNewIDLop(root);
        }
        sb.getLops().forEach(Lop::resetVisitStatus);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldSkipRewrite() {
    return !ConfigurationManager.isPrefetchEnabled() && !ConfigurationManager.isBroadcastEnabled() && !ConfigurationManager.isCheckpointEnabled();
}

private StatementBlock getInnerStatementBlock(StatementBlock sb) {
    return sb instanceof WhileStatementBlock ? ((WhileStatement) sb.getStatement(0)).getBody().get(0) : ((ForStatement) sb.getStatement(0)).getBody().get(0);
}

