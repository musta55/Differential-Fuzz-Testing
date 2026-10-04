/**
 * Constructor to perform a binary operation.
 *
 * @param input1 low-level operator 1
 * @param input2 low-level operator 2
 * @param op operation type
 * @param dt data type
 * @param vt value type
 * @param et exec type
 */
public Binary(Lop input1, Lop input2, OpOp2 op, DataType dt, ValueType vt, ExecType et) {
    this(input1, input2, op, dt, vt, et, 1, false);
}
// ---- helper method(s) introduced by the refactoring ----
private boolean isSparkExecution() {
    return getExecType() == ExecType.SPARK;
}

private boolean isFrameMatrixCombination(ArrayList<Lop> inputs) {
    return inputs.get(0).getDataType() == DataType.FRAME && inputs.get(1).getDataType() == DataType.MATRIX;
}

private boolean isCPExecution() {
    return getExecType() == ExecType.CP;
}

private boolean isFedExecution() {
    return getExecType() == ExecType.FED;
}

