private String getOpcode() {
    return opcodeMap.getOrDefault(operationType, unsupportedOperation());
}
// ---- helper method(s) introduced by the refactoring ----
private void setOperationType(OpOpN operationType) {
    this.operationType = operationType;
}

private void addInputLops(Lop[] inputLops) {
    for (Lop inputLop : inputLops) {
        addInput(inputLop);
        inputLop.addOutput(this);
    }
}

private void setExecutionProperties(ExecType et) {
    if (et == ExecType.CP || et == ExecType.SPARK) {
        lps.setProperties(inputs, et);
    } else {
        throw new LopsException("Unsupported exec type for nary lop:" + et.name());
    }
}

private String unsupportedOperation() {
    throw new UnsupportedOperationException("Nary operation type (" + operationType + ") is not defined.");
}

