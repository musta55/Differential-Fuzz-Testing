@Override
public void processInstruction(ExecutionContext ec) {
    String opcode = getOpcode();
    ScalarObject so = ec.getScalarInput(input1);
    ScalarObject sores = handleOpcode(opcode, so, ec);
    ec.setScalarOutput(output.getName(), sores);
}
// ---- helper method(s) introduced by the refactoring ----
private ScalarObject handleOpcode(String opcode, ScalarObject so, ExecutionContext ec) {
    if (opcode.equalsIgnoreCase(Opcodes.PRINT.toString())) {
        return handlePrintOpcode(so);
    } else if (opcode.equalsIgnoreCase(Opcodes.STOP.toString())) {
        handleStopOpcode(so);
        // unreachable
        return null;
    } else if (opcode.equalsIgnoreCase(Opcodes.ASSERT.toString())) {
        return handleAssertOpcode(so);
    } else {
        return handleUnaryOperationOpcode(so);
    }
}

private ScalarObject handlePrintOpcode(ScalarObject so) {
    String outString = so.getLanguageSpecificStringValue();
    if (!DMLScript.suppressPrint2Stdout()) {
        System.out.println(outString);
    }
    return new StringObject(outString);
}

private void handleStopOpcode(ScalarObject so) {
    String message = so.getStringValue();
    if (message != null && !message.isEmpty()) {
        throw new DMLScriptException(message);
    } else {
        throw new DMLScriptException("Stop Called");
    }
}

private ScalarObject handleAssertOpcode(ScalarObject so) {
    boolean assertionResult = so.getBooleanValue();
    ScalarObject sores = new BooleanObject(assertionResult);
    if (!assertionResult) {
        String fileName = (getFilename() == null) ? "" : getFilename() + " ";
        throw new DMLScriptException("assertion failed at " + fileName + getBeginLine() + ":" + getBeginColumn() + "-" + getEndLine() + ":" + getEndColumn());
    }
    return sores;
}

private ScalarObject handleUnaryOperationOpcode(ScalarObject so) {
    UnaryOperator dop = (UnaryOperator) _optr;
    if (so instanceof IntObject && output.getValueType() == ValueType.INT64) {
        return new IntObject((long) dop.fn.execute(so.getLongValue()));
    } else if (so instanceof BooleanObject && output.getValueType() == ValueType.BOOLEAN) {
        return new BooleanObject(dop.fn.execute(so.getBooleanValue()));
    } else {
        return new DoubleObject(dop.fn.execute(so.getDoubleValue()));
    }
}

