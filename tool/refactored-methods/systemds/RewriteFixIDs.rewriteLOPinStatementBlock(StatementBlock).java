@Override
public List<StatementBlock> rewriteLOPinStatementBlock(StatementBlock sb) {
    if (shouldSkipRewrite()) {
        return List.of(sb);
    }
    if (HopRewriteUtils.isLastLevelLoopStatementBlock(sb)) {
        StatementBlock csb = getInnerStatementBlock(sb);
        assignNewIDStatementBlock(csb);
    } else {
        assignNewIDStatementBlock(sb);
    }
    return List.of(sb);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean shouldSkipRewrite() {
    return !ConfigurationManager.isPrefetchEnabled() && !ConfigurationManager.isBroadcastEnabled() && !ConfigurationManager.isCheckpointEnabled();
}

private StatementBlock getInnerStatementBlock(StatementBlock sb) {
    return sb instanceof WhileStatementBlock ? ((WhileStatement) sb.getStatement(0)).getBody().get(0) : ((ForStatement) sb.getStatement(0)).getBody().get(0);
}

