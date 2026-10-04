@Override
public void execute(ExecutionContext ec) {
    BooleanObject predResult = executePredicate(ec);
    if (DMLScript.LINEAGE_DEDUP)
        ec.getLineage().setDedupPathBranch(_lineagePathPos, predResult.getBooleanValue());
    //execute if statement
    if (predResult.getBooleanValue()) {
        executeChildBlocks(_childBlocksIfBody, ec, "if");
    } else {
        executeChildBlocks(_childBlocksElseBody, ec, "else");
    }
    //execute exit instructions
    executeExitInstructions("if", ec);
}
// ---- helper method(s) introduced by the refactoring ----
private void executeChildBlocks(ArrayList<ProgramBlock> childBlocks, ExecutionContext ec, String blockType) {
    try {
        for (ProgramBlock pb : childBlocks) {
            pb.execute(ec);
        }
    } catch (DMLScriptException e) {
        throw e;
    } catch (Exception e) {
        throw new DMLRuntimeException(this.printBlockErrorLocation() + "Error evaluating " + blockType + " statement body ", e);
    }
}

