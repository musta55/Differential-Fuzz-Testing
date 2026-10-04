@Override
public void processInstruction(ExecutionContext ec) {
    String opcode = getOpcode();
    ScalarObject rl = ec.getScalarInput(rowLower);
    ScalarObject ru = ec.getScalarInput(rowUpper);
    if (opcode.equalsIgnoreCase(Opcodes.RIGHT_INDEX.toString())) {
        processRightIndexing(ec, rl, ru);
    } else if (opcode.equalsIgnoreCase(Opcodes.LEFT_INDEX.toString())) {
        processLeftIndexing(ec, rl, ru);
    } else {
        throw new DMLRuntimeException("Invalid opcode (" + opcode + ") encountered in ListIndexingCPInstruction.");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processRightIndexing(ExecutionContext ec, ScalarObject rl, ScalarObject ru) {
    ListObject list = (ListObject) ec.getVariable(input1.getName());
    if (rl.getValueType() == ValueType.STRING || ru.getValueType() == ValueType.STRING) {
        ec.setVariable(output.getName(), list.slice(rl.getStringValue(), ru.getStringValue()));
    } else {
        ec.setVariable(output.getName(), list.slice((int) rl.getLongValue() - 1, (int) ru.getLongValue() - 1));
    }
}

private void processLeftIndexing(ExecutionContext ec, ScalarObject rl, ScalarObject ru) {
    ListObject lin = (ListObject) ec.getVariable(input1.getName());
    if (input2.getDataType().isList()) {
        ListObject rin = (ListObject) ec.getVariable(input2.getName());
        if (rl.getValueType() == ValueType.STRING)
            ec.setVariable(output.getName(), lin.copy().set(rl.getStringValue(), ru.getStringValue(), rin));
        else
            ec.setVariable(output.getName(), lin.copy().set((int) rl.getLongValue() - 1, (int) ru.getLongValue() - 1, rin));
    } else if (input2.getDataType().isScalar()) {
        ScalarObject scalar = ec.getScalarInput(input2);
        LineageItem li = DMLScript.LINEAGE ? ec.getLineage().getOrCreate(input2) : null;
        if (rl.getValueType() == ValueType.STRING)
            ec.setVariable(output.getName(), lin.copy().set(rl.getStringValue(), scalar, li));
        else
            ec.setVariable(output.getName(), lin.copy().set((int) rl.getLongValue() - 1, scalar, li));
    } else if (input2.getDataType().isMatrix() || input2.getDataType().isFrame()) {
        CacheableData<?> dat = ec.getCacheableData(input2);
        dat.enableCleanup(false);
        LineageItem li = DMLScript.LINEAGE ? ec.getLineage().get(input2) : null;
        if (rl.getValueType() == ValueType.STRING)
            ec.setVariable(output.getName(), lin.copy().set(rl.getStringValue(), dat, li));
        else
            ec.setVariable(output.getName(), lin.copy().set((int) rl.getLongValue() - 1, dat, li));
    } else {
        throw new DMLRuntimeException("Unsupported list left indexing rhs type: " + input2.getDataType().name());
    }
}

