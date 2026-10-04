@Override
public List<StatementBlock> rewriteStatementBlock(StatementBlock sb, ProgramRewriteStatus state) {
    ArrayList<StatementBlock> ret = new ArrayList<>();
    //prune last level blocks with empty hops
    if (sb instanceof ForStatementBlock && ((ForStatement) sb.getStatement(0)).getBody().isEmpty()) {
        if (LOG.isDebugEnabled())
            LOG.debug("Applied removeEmptyForLopp (lines " + sb.getBeginLine() + "-" + sb.getEndLine() + ").");
    } else
        //keep original sb
        ret.add(sb);
    return ret;
}