@Override
public List<StatementBlock> rewriteStatementBlock(StatementBlock sb, ProgramRewriteStatus state) {
    if (sb.getHops() == null || sb.getHops().isEmpty())
        return Arrays.asList(sb);
    //Transformencode is a multi-return FunctionOp and always appears as root
    //of the DAG. We then check that the meta data object is never used,
    //that is, the meta data is not in the live-out variables of the statementblock
    Hop root = sb.getHops().get(0);
    if (root instanceof FunctionOp && TF_OPCODE.equals(((FunctionOp) root).getFunctionName())) {
        FunctionOp func = (FunctionOp) root;
        if (!sb.liveOut().containsVariable(func.getOutputVariableNames()[1]) && func.getInput().size() == 2) {
            //not added yet
            func.getInput().add(new LiteralOp(false));
            LOG.debug("Applied removeTransformEncodeMeta (line " + func.getBeginLine() + ").");
        }
    }
    return Arrays.asList(sb);
}