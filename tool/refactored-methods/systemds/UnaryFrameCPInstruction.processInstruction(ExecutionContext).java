@Override
public void processInstruction(ExecutionContext ec) {
    String opcode = getOpcode();
    if (opcode.equals(Opcodes.TYPEOF.toString())) {
        processTypeof(ec);
    } else if (opcode.equals(Opcodes.DETECTSCHEMA.toString())) {
        processDetectSchema(ec);
    } else if (opcode.equals(Opcodes.COLNAMES.toString())) {
        processColnames(ec);
    } else {
        throw new DMLScriptException("Opcode '" + opcode + "' is not a valid UnaryFrameCPInstruction");
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void processTypeof(ExecutionContext ec) {
    FrameBlock inBlock = ec.getFrameInput(input1.getName());
    FrameBlock retBlock = inBlock.getSchemaTypeOf();
    ec.releaseFrameInput(input1.getName());
    ec.setFrameOutput(output.getName(), retBlock);
}

private void processDetectSchema(ExecutionContext ec) {
    FrameBlock inBlock = ec.getFrameInput(input1.getName());
    FrameBlock retBlock = inBlock.detectSchema(((MultiThreadedOperator) _optr).getNumThreads());
    ec.releaseFrameInput(input1.getName());
    ec.setFrameOutput(output.getName(), retBlock);
}

private void processColnames(ExecutionContext ec) {
    FrameBlock inBlock = ec.getFrameInput(input1.getName());
    FrameBlock retBlock = inBlock.getColumnNamesAsFrame();
    ec.releaseFrameInput(input1.getName());
    ec.setFrameOutput(output.getName(), retBlock);
}

