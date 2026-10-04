@Override
public List<StatementBlock> rewriteStatementBlock(StatementBlock sb, ProgramRewriteStatus state) {
    ArrayList<StatementBlock> ret = new ArrayList<>();
    //prune last level blocks with empty hops
    if (sb instanceof ForStatementBlock && ((ForStatement) sb.getStatement(0)).getBody().isEmpty()) {
        logRemovedEmptyForLoop(sb);
    } else
        //keep original sb
        ret.add(sb);
    return ret;
}
// ---- helper method(s) introduced by the refactoring ----
private void logRemovedEmptyForLoop(StatementBlock sb) {
    if (LOG.isDebugEnabled())
        LOG.debug("Applied removeEmptyForLopp (lines " + sb.getBeginLine() + "-" + sb.getEndLine() + ").");
}

