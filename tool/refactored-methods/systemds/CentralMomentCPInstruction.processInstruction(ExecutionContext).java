@Override
public void processInstruction(ExecutionContext ec) {
    String output_name = output.getName();
    /*
         * The "order" of the central moment in the instruction can
         * be set to INVALID when the exact value is unknown at
         * compilation time. We first need to determine the exact
         * order and update the CMOperator, if needed.
         */
    MatrixBlock matBlock = ec.getMatrixInput(input1.getName());
    CPOperand scalarInput = (input3 == null ? input2 : input3);
    ScalarObject order = ec.getScalarInput(scalarInput);
    CMOperator cm_op = ((CMOperator) _optr);
    if (cm_op.getAggOpType() == AggregateOperationTypes.INVALID) {
        cm_op = cm_op.setCMAggOp(determineCentralMomentOrder(order));
    }
    CmCovObject cmobj = null;
    if (input3 == null) {
        cmobj = matBlock.cmOperations(cm_op);
    } else {
        MatrixBlock wtBlock = ec.getMatrixInput(input2.getName());
        cmobj = matBlock.cmOperations(cm_op, wtBlock);
        ec.releaseMatrixInput(input2.getName());
    }
    ec.releaseMatrixInput(input1.getName());
    double val = cmobj.getRequiredResult(cm_op);
    ec.setScalarOutput(output_name, new DoubleObject(val));
}
// ---- helper method(s) introduced by the refactoring ----
private int determineCentralMomentOrder(ScalarObject order) {
    try {
        return (int) order.getLongValue();
    } catch (NumberFormatException e) {
        // unknown at compilation time
        return -1;
    }
}

