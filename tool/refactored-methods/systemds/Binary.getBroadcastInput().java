@Override
public Lop getBroadcastInput() {
    if (!isSparkExecution())
        return null;
    ArrayList<Lop> inputs = getInputs();
    if (isFrameMatrixCombination(inputs))
        return inputs.get(1);
    else
        return null;
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

