protected BinaryMatrixScalarCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    super(CPType.Binary, op, in1, in2, out, opcode, istr);
    if (op instanceof ScalarOperator) {
        setNumThreadsFromInstructionString(op, istr);
    }
}
// ---- helper method(s) introduced by the refactoring ----
private void setNumThreadsFromInstructionString(Operator op, String istr) {
    String[] parts = InstructionUtils.getInstructionParts(istr);
    if (parts.length > 4)
        ((ScalarOperator) op).setNumThreads(Integer.parseInt(parts[parts.length - 1]));
}

