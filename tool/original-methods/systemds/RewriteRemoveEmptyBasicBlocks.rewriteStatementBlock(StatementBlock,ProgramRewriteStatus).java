@Override
public List<StatementBlock> rewriteStatementBlock(StatementBlock sb, ProgramRewriteStatus state) {
    ArrayList<StatementBlock> ret = new ArrayList<>();
    //prune last level blocks with empty hops
    if (HopRewriteUtils.isLastLevelStatementBlock(sb) && (sb.getHops() == null || sb.getHops().isEmpty())) {
        if (LOG.isDebugEnabled())
            LOG.debug("Applied removeEmptyBasicBlocks (lines " + sb.getBeginLine() + "-" + sb.getEndLine() + ").");
    } else
        //keep original sb
        ret.add(sb);
    return ret;
}