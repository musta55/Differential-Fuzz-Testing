public BinaryTensorTensorCPInstruction(Operator op, CPOperand in1, CPOperand in2, CPOperand out, String opcode, String istr) {
    this(new Config(op, in1, in2, out, opcode, istr));
}
// ---- helper method(s) introduced by the refactoring ----
protected BinaryTensorTensorCPInstruction(Config config) {
    super(CPType.Binary, config.op, config.in1, config.in2, config.out, config.opcode, config.istr);
    this.config = config;
}

