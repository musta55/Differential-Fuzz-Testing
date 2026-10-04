@Override
public List<StatementBlock> rewriteLOPinStatementBlock(StatementBlock sb) {
    // Skip if no new Lop nodes are added
    if (!ConfigurationManager.isPrefetchEnabled() && !ConfigurationManager.isBroadcastEnabled() && !ConfigurationManager.isCheckpointEnabled())
        return List.of(sb);
    if (HopRewriteUtils.isLastLevelLoopStatementBlock(sb)) {
        // Some rewrites add new Lops in the last-level loop body
        StatementBlock csb = sb instanceof WhileStatementBlock ? ((WhileStatement) sb.getStatement(0)).getBody().get(0) : ((ForStatement) sb.getStatement(0)).getBody().get(0);
        assignNewIDStatementBlock(csb);
    } else
        assignNewIDStatementBlock(sb);
    return List.of(sb);
}