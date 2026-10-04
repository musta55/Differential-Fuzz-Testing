@Override
public void execute(ExecutionContext ec) {
    BooleanObject predResult = executePredicate(ec);
    if (DMLScript.LINEAGE_DEDUP)
        ec.getLineage().setDedupPathBranch(_lineagePathPos, predResult.getBooleanValue());
    //execute if statement
    if (predResult.getBooleanValue()) {
        try {
            for (int i = 0; i < _childBlocksIfBody.size(); i++) {
                _childBlocksIfBody.get(i).execute(ec);
            }
        } catch (DMLScriptException e) {
            throw e;
        } catch (Exception e) {
            throw new DMLRuntimeException(this.printBlockErrorLocation() + "Error evaluating if statement body ", e);
        }
    } else {
        try {
            for (int i = 0; i < _childBlocksElseBody.size(); i++) {
                _childBlocksElseBody.get(i).execute(ec);
            }
        } catch (DMLScriptException e) {
            throw e;
        } catch (Exception e) {
            throw new DMLRuntimeException(this.printBlockErrorLocation() + "Error evaluating else statement body ", e);
        }
    }
    //execute exit instructions
    executeExitInstructions("if", ec);
}